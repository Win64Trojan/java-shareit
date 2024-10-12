package ru.practicum.shareit.user.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidatetionConflict;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class BaseUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto newUserDto) {

        if (!userRepository.getUserByEmail(newUserDto.getEmail()).isEmpty()) {
            throw new ValidatetionConflict("Пользователь с таким email уже зарегестрирован");
        }
        return UserMapper.toUserDto(userRepository.create(UserMapper.toUser(newUserDto)));
    }

    @Override
    public UserDto update(long id,UserDto userUpdDto) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь " + id + "не найден"));

        if (!userRepository.getUserByEmail(userUpdDto.getEmail()).isEmpty() &&
                userRepository.getUserByEmail(userUpdDto.getEmail()).get().getId() != id) {
            throw new ValidatetionConflict("Пользователь с таким email уже зарегестрирован");
        }
        return UserMapper.toUserDto(userRepository.update(id, UserMapper.toUser(userUpdDto)));
    }

    @Override
    public void delete(long id) {
        userRepository.delete(id);
    }

    @Override
    public UserDto findById(long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь " + id + "не найден"));
        return UserMapper.toUserDto(userRepository.findById(id).get());
    }
}