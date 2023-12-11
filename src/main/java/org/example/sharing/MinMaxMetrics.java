package org.example.sharing;

public class MinMaxMetrics {

        private volatile long min;
        private volatile long max ;

        // Add all necessary member variables

        /**
         * Initializes all member variables
         */
        public MinMaxMetrics() {
            // Add code here
            this.min =Long.MIN_VALUE;
            this.max=Long.MAX_VALUE;

        }

        /**
         * Adds a new sample to our metrics.
         */
        public void addSample(long newSample) {
            // Add code here
        min = Math.min(newSample ,min);
        max= Math.max(newSample ,max);

        }

        /**
         * Returns the smallest sample we've seen so far.
         */
        public long getMin() {
            return min;
        }

        /**
         * Returns the biggest sample we've seen so far.
         */
        public long getMax() {
            // Add code here
            return max;
        }
    }

