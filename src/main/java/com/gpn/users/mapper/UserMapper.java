package com.gpn.users.mapper;

import com.gpn.users.model.UserEntity;
import com.gpn.users.model.dto.UserDetailsDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static UserDetailsDto toDto(final UserEntity userEntity) {
        return new UserDetailsDto(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail());
    }

}
