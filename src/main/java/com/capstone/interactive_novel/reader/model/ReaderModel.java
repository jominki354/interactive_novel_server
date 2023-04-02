package com.capstone.interactive_novel.reader.model;

import com.capstone.interactive_novel.reader.domain.ReaderEntity;
import com.capstone.interactive_novel.reader.domain.ReaderRole;
import lombok.Data;

public class ReaderModel {
    @Data
    public static class SignUp {
        private String email;
        private String password;
        private String userName;

        public ReaderEntity toEntity() {
            return ReaderEntity.builder()
                    .email(email)
                    .password(password)
                    .userName(userName)
                    .role(ReaderRole.UNCERTIFIED)
                    .interlock("self")
                    .build();
        }
    }

    @Data
    public static class SignIn {
        private String email;
        private String password;
    }
}