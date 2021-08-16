package com.github.bluecatlee.gs4d.sequence.service;

import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public interface SequenceTimeService {

    void insertTime(String paramString, Date paramDate);

    String getTime();

    void updateTime(String paramString, Date paramDate);

    Boolean getCount();

    void editTime();

}
