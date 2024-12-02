package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto(user.getId(),
                user.getName(),
                user.getEmail());
        return userDto;
    }

    public static User toUser(UserDto userDto) {
        User user = new User(userDto.getId(),
                userDto.getName(),
                userDto.getEmail());
        return user;
    }
}