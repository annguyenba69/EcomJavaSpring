package com.shop.common.entity;

public enum MenuType {

	HEADER{

		@Override
		public String toString() {
			return "Header Menu";
		}
		
	},
	FOOTER{
		@Override
		public String toString() {
			return "Footer Menu";
		}
	}
}
