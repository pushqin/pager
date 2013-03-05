package com.example.pager.model;

public class Dimension
{
	private double height;
	private double width;
	private double length;
	
	public Dimension()
	{
		this.height = 0;
		this.width = 0;
		this.length = 0;
	}
	
	public Dimension(double height, double width, double length)
	{
		this.height = height;
		this.width = width;
		this.length = length;
	}
	
	public double getHeight()
	{
		return height;
	}
	
	public void setHeight(double height)
	{
		this.height = height;
	}
	
	public double getWidth()
	{
		return width;
	}
	
	public void setWidth(double width)
	{
		this.width = width;
	}
	
	public double getLength()
	{
		return length;
	}
	
	public void setLength(double length)
	{
		this.length = length;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Dimension: [Height: ").append(this.height).append(", Width: ").append(this.width).append(", Length: ").append(this.length).append("]");
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
		if (!(o instanceof Dimension))
		{
			return false;
		}
		Dimension other = (Dimension) o;
		return other.getHeight() == this.height && 
				other.getWidth() == this.width && 
				other.getLength() == this.length;
	}
}
