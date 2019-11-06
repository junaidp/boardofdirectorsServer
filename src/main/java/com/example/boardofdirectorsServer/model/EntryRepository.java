package com.example.boardofdirectorsServer.model;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface EntryRepository extends JpaRepository<EntryEntity, Integer> {
	
	List<EntryEntity> find(String text, String textahao);

}
