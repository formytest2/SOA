package com.github.bluecatlee.gs4d.sequence.service.impl;

import com.github.bluecatlee.gs4d.sequence.dao.SequenceTimeDao;
import com.github.bluecatlee.gs4d.sequence.exception.SequenceException;
import com.github.bluecatlee.gs4d.sequence.service.SequenceTimeService;
import com.github.bluecatlee.gs4d.sequence.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("sequenceTimeService")
public class SequenceTimeServiceImpl implements SequenceTimeService {

    @Autowired
    private SequenceTimeDao O;

    protected static Logger logger = LoggerFactory.getLogger(SequenceTimeServiceImpl.class);

    public void insertTime(String paramString, Date paramDate) {
        try {
            this.O.insertTime(paramString, paramDate);
        } catch (Exception exception) {
            logger.error("数据库添加序列时间失败" + exception.getMessage(), exception);
            throw new SequenceException("数据库添加序列时间失败" + exception.getMessage());
        }
    }

    public String getTime() {
        try {
            return this.O.getSequence();
        } catch (Exception exception) {
            logger.error("查询数据库序列时间失败" + exception.getMessage(), exception);
            throw new SequenceException("查询数据库序列时间失败" + exception.getMessage());
        }
    }

    public void updateTime(String paramString, Date paramDate) {
        try {
            this.O.updateTime(paramString, paramDate);
        } catch (Exception exception) {
            logger.error("数据库更新序列时间失败" + exception.getMessage(), exception);
            throw new SequenceException("数据库更新序列时间失败" + exception.getMessage());
        }
    }

    public Boolean getCount() {
        boolean bool = true;
        Integer integer = Integer.valueOf(0);
        try {
            integer = this.O.getCount();
            if (integer.intValue() > 0)
                bool = false;
        } catch (Exception exception) {
            logger.error("查询数据库序列时间失败" + exception.getMessage(), exception);
            throw new SequenceException("查询数据库序列时间失败" + exception.getMessage());
        }
        return Boolean.valueOf(bool);
    }

    public void editTime() {
        String str = "";
        try {
            str = DateUtil.getCurrentDateAsString();
            if (!getCount().booleanValue()) {
                updateTime(str, new Date());
            } else {
                insertTime(str, new Date());
            }
        } catch (Exception exception) {
            logger.error("数据库序列时间更新失败" + exception.getMessage(), exception);
            throw new SequenceException("数据库序列时间更新失败" + exception.getMessage());
        }
    }
}
