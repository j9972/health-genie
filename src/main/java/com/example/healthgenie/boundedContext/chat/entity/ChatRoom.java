package com.example.healthgenie.boundedContext.chat.entity;

import com.example.healthgenie.base.entity.BaseEntity;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CHAT_ROOM_TB")
@Builder
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatting_room_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(name = "sender_out")
    private boolean isSenderOut;

    @Column(name = "receiver_out")
    private boolean isReceiverOut;

    @Builder.Default
    @OneToMany(mappedBy = "chatRoom",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public void exitRoom(User user) {
        if(Objects.equals(user.getId(), sender.getId())) {
            isSenderOut = true;
        }
        if (Objects.equals(user.getId(), receiver.getId())) {
            isReceiverOut = true;
        }
    }
}
