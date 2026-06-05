package com.fullstack.mail.repository;

import com.fullstack.mail.entity.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {

    List<EmailLog> findByTo(String to);

    List<EmailLog> findByStatus(EmailLog.EmailStatus status);
}
