package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import java.util.Date;

public class Booth extends PointOfInterest
{
	protected String name;
	protected String type;
	protected String section;
	protected int id;
	protected int couponPrice;
	protected String dimensions;
	protected Date openTime;
	protected Date closeTime;
	protected boolean isOpen;
	protected boolean isVIP;
	
	Booth()
	{
    }
}