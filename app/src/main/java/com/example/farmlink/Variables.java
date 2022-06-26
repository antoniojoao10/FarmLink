package com.example.farmlink;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

public class Variables {
    public static ArrayList<String> imagesNames;
    public static ArrayList<String> productNames;
    public static ArrayList<String> productPrices;
    public static ArrayList<String> productQuantaty;
    public static ArrayList<String> productSeller;
    public static HashMap<String,String> productBought;
    public static HashMap<String,String> productSold;
    public static ArrayList<String> imagesNamesFilter;
    public static ArrayList<String> productNamesFilter;
    public static ArrayList<String> productPricesFilter;
    public static ArrayList<String> names;
    public static ArrayList<Integer> order;
    public static ArrayList<Integer> notify;

    public static FirebaseAuth mAuth;
}
