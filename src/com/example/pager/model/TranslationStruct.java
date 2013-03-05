package com.example.pager.model;

public class TranslationStruct
{
	public String shortText;
	public long languageCodeId;
	public String translatedText;
	
	public TranslationStruct(String shortText, long languageId, String translation)
	{
		this.shortText = shortText;
		this.languageCodeId = languageId;
		this.translatedText = translation;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("ShortText: ").append(this.shortText).append(", LanguageCodeId: ").append(this.languageCodeId).append(", Translation: ")
		        .append(this.translatedText);
		return sb.toString();
	}
}
