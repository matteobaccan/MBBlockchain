package it.baccan.blockchain;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 *
 * @author Matteo
 */
@SpringBootApplication
@Slf4j
public class MBBlockchain {

    /**
     * Start MBBlockchain.
     *
     * @param args
     */
    public static void main(String[] args) {
        // Options
        Option help = new Option("help", "print this message");
        Option port = OptionBuilder.withArgName("portNumber")
                .hasArg()
                .withDescription("port to bind")
                .create("port");

        Option ip = OptionBuilder.withArgName("ipv4")
                .hasArg()
                .withDescription("ip to bind")
                .create("ip");

        // create Options object
        Options options = new Options();
        options.addOption(help);
        options.addOption(port);
        options.addOption(ip);

        // create the parser
        CommandLineParser parser = new DefaultParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            // has the buildfile argument been passed?
            if (line.hasOption("help")) {
                // initialise the member variable
                // automatically generate the help statement
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("ant", options);
                System.exit(0);
            }

            String portNumber = "8080";
            if (line.hasOption("port")) {
                portNumber = line.getOptionValue("port");
            }

            String ipNumber = "0.0.0.0";
            if (line.hasOption("ip")) {
                ipNumber = line.getOptionValue("ip");
            }

            Map<String, Object> props = new HashMap<>();
            props.put("server.port", new Long(portNumber).longValue());
            props.put("server.address", ipNumber);

            new SpringApplicationBuilder()
                    .sources(MBBlockchain.class)
                    .properties(props)
                    .run(args);
        } catch (ParseException exp) {
            log.error("Error parcing command line", exp);
        }

    }
}
