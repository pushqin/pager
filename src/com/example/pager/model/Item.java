package com.example.pager.model;

public class Item
{
	private long id;
	private String name;
	private String condition;
	private Dimension itemDimension;
	private String serialNumber;
	private String notes;
	
	public Item()
	{
		this(null);
	}
	
	public Item(String name)
	{
		this(name, null, null, null, null);
	}
	
	public Item(String name, String condition, Dimension dimension, String serial, String notes)
	{
		this.id = -1;	
		this.name = name;
		this.condition = condition;
		this.itemDimension = dimension;
		this.serialNumber = serial;
		this.notes = notes;
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
	
	public String getCondition()
	{
		return condition;
	}
	
	public void setCondition(String condition)
	{
		this.condition = condition;
	}
	
	public Dimension getItemDimension()
	{
		return itemDimension;
	}
	
	public void setItemDimension(Dimension itemDimension)
	{
		this.itemDimension = itemDimension;
	}
	
	public String getSerialNumber()
	{
		return serialNumber;
	}
	
	public void setSerialNumber(String serialNumber)
	{
		this.serialNumber = serialNumber;
	}
	
	public String getNotes()
	{
		return notes;
	}
	
	public void setNotes(String notes)
	{
		this.notes = notes;
	}
	
	@Override
	public String toString()
	{
	    StringBuilder sb = new StringBuilder();
	    sb.append("Item: [ID ").append(this.id).
	    	append(", Name: ").append(this.name).
	    	append(", Condition: ").append(this.condition).
	    	append(", Dimension: ").append(this.itemDimension.toString()).
	    	append(", SerialNumber: ").append(this.serialNumber).
	    	append(", Notes: ").append(this.notes).append("]");
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
		if (!(o instanceof Item))
		{
			return false;
		}
		Item other = (Item) o;
		return other.getId() == this.id;
	}
}
