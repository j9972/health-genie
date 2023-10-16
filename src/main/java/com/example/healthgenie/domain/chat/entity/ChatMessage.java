package com.example.healthgenie.domain.chat.entity;

import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "CHAT_MESSAGE_TB")
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @NotNull
    @Column(name = "message_content")
    private String messageContent;

    @Column(name = "send_at")
    private Date sendAt;

    @Column(name = "reading")
    private boolean reading;

    // FK -> user[1] : chatMessage[N], trainer[1] : chatMessage[N] , chattingRoom[1] : chatMessage[N]
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="trainer_id")
    private User trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="room_id")
    private ChattingRoom chattingRoom;

    @OneToMany(mappedBy = "chatMessage",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ChatMessagePhoto> chatMessagePhotoList;

}
