package com.barabanov.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "chat"})
@EqualsAndHashCode(exclude = {"user", "chat"})
@Entity
@Table(name = "users_chat")
public class UserChat
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    private Instant createdAt;

    private String createdBy;


    public void setUser(User user)
    {
        this.user = user;
        this.user.getUserChats().add(this);
    }

    public void setChat(Chat chat)
    {
        this.chat = chat;
        this.chat.getUserChats().add(this);
    }
}
