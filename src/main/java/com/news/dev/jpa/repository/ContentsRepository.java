package com.news.dev.jpa.repository;

import com.news.dev.jpa.entity.ContentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;

public interface ContentsRepository extends JpaRepository<ContentsEntity, Long> {

    Collection<ContentsEntity> findAllByUpdDtm(LocalDate now);

}
