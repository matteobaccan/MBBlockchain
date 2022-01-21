/*
 * Copyright (c) 2018 Matteo Baccan
 * Distributed under the MIT software license, see the accompanying
 * file COPYING or http://www.opensource.org/licenses/mit-license.php.
 */
package it.baccan.blockchain.timedata;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Matteo Baccan
 */
public class MedianFilter {

    private final ArrayList<Long> vValues;
    private Long[] vSorted;
    private final int size;

    /**
     *
     * @param filterSize
     */
    public MedianFilter(int filterSize) {
        vValues = new ArrayList<>();
        vSorted = vValues.toArray(vSorted);
        size = filterSize;
    }

    /**
     *
     * @param value
     */
    public void addValue(Long value) {
        if (vValues.size() > size) {
            vValues.remove(0);
        }
        vValues.add(value);
        vSorted = vValues.toArray(vSorted);
        Arrays.sort(vSorted);
    }

    /**
     *
     * @return
     */
    public Long median() {
        Long ret;
        int vSortedSize = vSorted.length;
        if ((vSortedSize & 1) == 1) {
            ret = vSorted[vSortedSize / 2];
        } else {
            ret = (vSorted[vSortedSize / 2 - 1] + vSorted[vSortedSize / 2]) / 2;
        }
        return ret;
    }

}
