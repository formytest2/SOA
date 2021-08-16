package com.github.bluecatlee.gs4d.message.api.response;

import com.github.bluecatlee.gs4d.common.bean.MessagePack;
import com.github.bluecatlee.gs4d.message.api.model.SonTopicRelationModel;

import java.util.List;

public class RelationTopicFindResponse extends MessagePack {
    private static final long serialVersionUID = 2676443343453796031L;
    private List<SonTopicRelationModel> topicRelations;

    public List<SonTopicRelationModel> getTopicRelations() {
        return this.topicRelations;
    }

    public void setTopicRelations(List<SonTopicRelationModel> topicRelations) {
        this.topicRelations = topicRelations;
    }
}

