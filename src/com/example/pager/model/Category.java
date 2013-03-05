package com.example.pager.model;

import java.util.ArrayList;
import java.util.List;

public class Category
{
	private long id;
	private String name;
	private List<Item> categoryItems;
	
	public Category(long id, String name, List<Item> items)
    {
	    this.id = id;
	    this.name = name;
	    this.categoryItems = new ArrayList<Item>(items);
    }
	
	public long getId()
    {
	    return id;
    }
	public void setId(int id)
    {
	    this.id = id;
    }
	public String getName()
    {
	    return name;
    }
	public void setName(String name)
    {
	    this.name = name;
    }
	public List<Item> getCategoryItems()
    {
	    return categoryItems;
    }
	public void setCategoryItems(List<Item> categoryItems)
    {
	    this.categoryItems = categoryItems;
    }
	
	@Override
	public String toString()
	{
	    StringBuilder sb = new StringBuilder();
	    sb.append("Category: [ID: ").append(this.id).
	    	append(", Name: ").append(this.name).
	    	append(", CategoryItems: ").append(this.categoryItems).append("]");
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
		Category other = (Category) o;
		return other.getId() == this.id;
	}
	
}
