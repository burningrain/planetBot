package com.github.br.starmarines.gamecore.db.converters;

import org.jooq.impl.EnumConverter;

import com.github.br.starmarines.gamecore.db.entities.UserGameStatus;

public class UserGameStatusConverter extends EnumConverter<Byte, UserGameStatus> {
	private static final long serialVersionUID = 2735127510088062042L;

	
	public UserGameStatusConverter(Class<Byte> arg0, Class<UserGameStatus> arg1) {
		super(Byte.class, UserGameStatus.class);		
	}
	

}
