package com.amr.project.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

//@Mapper
//public interface MessageMapper {
//    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);
//}
import com.amr.project.model.dto.MessageDto;
import com.amr.project.model.entity.Chat;
import com.amr.project.model.entity.Message;
import com.amr.project.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {User.class, Chat.class}, componentModel = "spring")
public interface MessageMapper {

    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(source = "chat.id", target = "chat")
    @Mapping(source = "to.id", target = "to")
    @Mapping(source = "from.id", target = "from")
    MessageDto messageToMessageDto(Message message);

    @Mapping(source = "chat", target = "chat.id")
    @Mapping(source = "to", target = "to.id")
    @Mapping(source = "from", target = "from.id")
    Message messageDtoToMessage(MessageDto messageDto);

    List<MessageDto> toListMassageDto(List<Message> messages);
}
