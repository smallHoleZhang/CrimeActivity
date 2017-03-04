package com.bignerdranch.android.crimeactivity.datebase;

/**
 * Created by hasee on 2017/2/27.
 */

public class CrimeDbScheme {
    public static final class  CrimeTable{
        public static final String NAME = " Crime";

            public static final class Cols {
                public static final String UUID = "uuid";
                public static final String TITLE = "title";
                public static final String DATE = "date";
                public static final String SOLVED = "solved";
                public static final String SUSPECT = "suspect";
            }


        }
    }

