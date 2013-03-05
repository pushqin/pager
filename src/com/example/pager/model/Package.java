package com.example.pager.model;

import java.util.ArrayList;
import java.util.List;

public class Package
{
	private long id;
	/**
	 * Type of package
	 */
	private String typeOfPackage;
	/**
	 * Type of the room from where the package was taken
	 */
	private String typeOfRoom;
	/**
	 * Name of the packager
	 */
	private String packager;
	/**
	 * All package categories
	 */
	private List<Category> categories;
	/**
	 * Items, that were put to this package
	 */
	private List<Item> itemsInside;
	
	public Package(long id, String packageType, String roomType, String packagerName, List<Category> categories, List<Item> items)
	{
		this.id = id;
		this.typeOfPackage = packageType;
		this.typeOfRoom = roomType;
		this.packager = packagerName;
		this.categories = new ArrayList<Category>(categories);
		this.itemsInside = new ArrayList<Item>(items);
	}
	
	public long getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getTypeOfPackage()
	{
		return typeOfPackage;
	}
	
	public void setTypeOfPackage(String typeOfPackage)
	{
		this.typeOfPackage = typeOfPackage;
	}
	
	public String getTypeOfRoom()
	{
		return typeOfRoom;
	}
	
	public void setTypeOfRoom(String typeOfRoom)
	{
		this.typeOfRoom = typeOfRoom;
	}
	
	public String getPackager()
	{
		return packager;
	}
	
	public void setPackager(String packager)
	{
		this.packager = packager;
	}
	
	public List<Category> getCategories()
	{
		return categories;
	}
	
	public void setCategories(List<Category> categories)
	{
		this.categories = categories;
	}
	
	public List<Item> getItemsInside()
	{
		return itemsInside;
	}
	
	public void setItemsInside(List<Item> itemsInside)
	{
		this.itemsInside = itemsInside;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Package: [ID: ").append(this.id).append(", Type Of Package: ").append(this.typeOfPackage).append(" Type Of Room: ")
		        .append(this.typeOfRoom).append(", Packager Name: ").append(this.packager).append(", Categories: ").append(this.categories)
		        .append(", Items Inside: ").append(this.itemsInside).append("]");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null)
		{
			return false;
		}
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof Category))
		{
			return false;
		}
		Package other = (Package) o;
		return other.getId() == this.id;
	}
}