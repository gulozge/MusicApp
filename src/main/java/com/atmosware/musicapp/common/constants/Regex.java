package com.atmosware.musicapp.common.constants;

public class Regex {
    public final static String email = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    public final static String password = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
}
