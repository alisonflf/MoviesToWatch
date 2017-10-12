package br.com.alisonfrancisco.moviestowatch.persistence;

public class MyDataBaseContract {

    public static class Movies {
        public static String TABLE_NAME = "movies";
        public static String COL_ID = "_id";
        public static String COL_TITLE = "title";
        public static String COL_OVERVIEW = "overview";
        public static String COL_POSTER_PATH = "posterpath";
        public static String COL_WATCHED = "watched";
    }

}