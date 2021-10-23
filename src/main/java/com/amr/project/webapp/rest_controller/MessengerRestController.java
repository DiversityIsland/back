package com.amr.project.webapp.rest_controller;

import com.amr.project.converter.ChatMapper;
import com.amr.project.converter.MessageMapper;
import com.amr.project.converter.UserMapper;
import com.amr.project.model.dto.ChatDto;
import com.amr.project.model.dto.MessageDto;
import com.amr.project.model.dto.UserDto;
import com.amr.project.model.entity.Chat;
import com.amr.project.model.entity.Message;
import com.amr.project.model.entity.User;
import com.amr.project.service.abstracts.ChatService;
import com.amr.project.service.abstracts.MessageService;
import com.amr.project.service.abstracts.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequestMapping("/api")
@Slf4j
@RestController
public class MessengerRestController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserService userService;
    private final MessageService messageService;
    private final ChatService chatService;
    private final MessageMapper messageMapper;
    private final UserMapper userMapper;
    private final ChatMapper chatMapper;

    @Autowired
    public MessengerRestController(SimpMessagingTemplate simpMessagingTemplate,
                                   UserService userService,
                                   MessageService messageService,
                                   ChatService chatService,
                                   MessageMapper messageMapper,
                                   UserMapper userMapper, ChatMapper chatMapper) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userService = userService;
        this.messageService = messageService;
        this.chatService = chatService;
        this.messageMapper = messageMapper;
        this.userMapper = userMapper;
        this.chatMapper = chatMapper;
    }

    @MessageMapping("/chat")
    public void processMessage(MessageDto messageDto) {
        User from = userService.getByKey(messageDto.getFrom());
        User to = userService.getByKey(messageDto.getTo());
        Chat chat;
        if (messageDto.getChat() != null) {
            chat = chatService.getByKey(messageDto.getChat());
        } else {
            chat = new Chat(List.of(from, to));
            chat.setMessages(new ArrayList<>());
        }

        Message message = Message.builder()
                .from(from)
                .to(to)
                .chat(chat)
                .textMessage(messageDto.getTextMessage())
                .build();

        chat.getMessages().add(message);
        chatService.update(chat);
        if (chat.getId() == null) {
            messageDto.setChat(chatService.findChatByHash(chat.getHash()).getId());
        }
        messageDto.setId(messageService.getLastMessage().orElse(new Message()).getId());
        simpMessagingTemplate.convertAndSend("/topic/messages/" + messageDto.getTo(), messageDto);
    }

    @GetMapping("/messenger/principal")
    public ResponseEntity<UserDto> getPrincipal(Principal principal) {
        UserDto userDto = userMapper.userToDto(userService.findByUsername(principal.getName()).get());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/messenger/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") Long id) {
        UserDto userDto = userMapper.userToDto(userService.getByKey(id));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/messenger/{toUserId}/{fromUserId}")
    public ResponseEntity<ChatDto> getChatByUsersId(@PathVariable("toUserId") Long toUserId,
                                                    @PathVariable("fromUserId") Long fromUserId) {

        User to = userService.getByKey(toUserId);
        User from = userService.getByKey(fromUserId);
        long hash = Stream.of(from, to).map(User::hashCode).mapToLong(h -> h).sum();
        log.info("hashcode {}", hash);
        ChatDto chatDto = chatMapper.toChatDto(chatService.findChatByHash(hash));
        return new ResponseEntity<>(Objects.requireNonNullElseGet(chatDto, ChatDto::new), HttpStatus.OK);
    }

    @GetMapping("/messenger/chat/{chatId}")
    public ResponseEntity<List<MessageDto>> getMessagesByChatId(@PathVariable("chatId") Long chatId) {
        List<MessageDto> messages = messageMapper.toListMassageDto(messageService.findMessagesByChatId(chatId));
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/messenger/private/chat/user/{id}")
    public ResponseEntity<List<UserDto>> getUsersConnectWithCurrentUser(@PathVariable("id") Long currentId) {

        List<Chat> chats = chatService.findChatsByUserId(currentId);
        List<UserDto> users = userMapper.toListUserDto(chats.stream().map(Chat::getId)
                .map(id -> userService.findUserConnectWithCurrentUserByChatId(id, currentId))
                .collect(Collectors.toList()));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/messenger/search/{searchName}")
    public ResponseEntity<Long> getLastPageNumBySearchName(@PathVariable String searchName) {
        long countTotalEl = userService.findCountTotalElementBySearchName(searchName);
        long lastPageNum;
        int pageSize = 4;
        if (countTotalEl % pageSize == 0) {
            lastPageNum = countTotalEl / pageSize;
        } else {
            lastPageNum = (countTotalEl / pageSize) + 1;
        }
        log.info("Last page number {}", lastPageNum);
        return new ResponseEntity<>(lastPageNum, HttpStatus.OK);
    }

    @GetMapping("/messenger/search/{searchName}/{pageNum}")
    public ResponseEntity<List<UserDto>> getUsersBySearchNameWithPagination(@PathVariable String searchName, @PathVariable Integer pageNum) {
        int pageSize = 4;
        List<UserDto> usersDto = userMapper.toListUserDto(userService.findUserBySearchNameWithPagination(searchName, pageNum, pageSize));
        return new ResponseEntity<>(usersDto, HttpStatus.OK);
    }
}