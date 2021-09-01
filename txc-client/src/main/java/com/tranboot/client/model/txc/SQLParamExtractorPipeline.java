package com.tranboot.client.model.txc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLParamExtractorPipeline {
    private static final Logger logger = LoggerFactory.getLogger(SQLParamExtractorPipeline.class);
    public static final String OUTTER_ARGS = "outter_args";
    public static final String QUERY_ROW = "query_row";
    public static final String FINAL_ARGS = "final_args";
    public static final String WHERE_ARGS = "where_args";
    SQLParamExtractorPipeline.ParamExtractor start0;
    private static final ThreadLocal<Map<String, Object>> context = new ThreadLocal();

    public SQLParamExtractorPipeline() {
    }

    public Object getParam(String key) {
        return ((Map)context.get()).get(key);
    }

    public void setParam(String key, Object value) {
        ((Map)context.get()).put(key, value);
    }

    public List<Object> getWhereArgs() {
        if (this.getParam("where_args") == null) {
            this.setParam("where_args", new ArrayList());
        }

        return (List)this.getParam("where_args");
    }

    public void addExtractor(SQLParamExtractorPipeline.ParamExtractor extractor) {
        if (this.start0 == null) {
            this.start0 = extractor;
        } else {
            this.start0.next(extractor);
        }
    }

    public void addHeadExtractor(SQLParamExtractorPipeline.ParamExtractor extractor) {
        if (this.start0 == null) {
            this.start0 = extractor;
        } else {
            SQLParamExtractorPipeline.ParamExtractor tmp = this.start0;
            this.start0 = extractor;
            this.start0.next = tmp;
        }

    }

    public void start(Object[] outter_args) {
        context.remove();
        context.set(new HashMap());
        this.setParam("outter_args", outter_args);

        for(SQLParamExtractorPipeline.ParamExtractor tmp = this.start0; tmp != null; tmp = tmp.next()) {
            if (tmp instanceof SQLParamExtractorPipeline.OriginalParamExtractor) {
                logger.debug(tmp.desc());
                tmp.extract();
            }
        }

    }

    public void render() {
        for(SQLParamExtractorPipeline.ParamExtractor tmp = this.start0; tmp != null; tmp = tmp.next()) {
            if (tmp instanceof SQLParamExtractorPipeline.UpdateSetItemExtractor) {
                logger.debug(tmp.desc());
                tmp.extract();
            }
        }

    }

    public List<Object> getFinalArgs() {
        return (List)this.getParam("final_args");
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();

        for(SQLParamExtractorPipeline.ParamExtractor tmp = this.start0; tmp != null; tmp = tmp.next) {
            sbuilder.append(tmp.toString() + System.lineSeparator());
        }

        String str = StringUtils.substringBeforeLast(sbuilder.toString(), System.lineSeparator());
        return str;
    }

    public abstract static class ParamExtractor {
        protected SQLParamExtractorPipeline context;
        protected SQLParamExtractorPipeline.ParamExtractor next;

        public ParamExtractor(SQLParamExtractorPipeline context) {
            this.context = context;
        }

        public void next(SQLParamExtractorPipeline.ParamExtractor extractor) {
            if (this.next == null) {
                this.next = extractor;
            } else {
                this.next.next(extractor);
            }

        }

        public Object[] getArgs() {
            return (Object[])((Object[])this.context.getParam("outter_args"));
        }

        public void setQuery(Map<String, Object> row) {
            this.context.setParam("query_row", row);
        }

        public boolean alreadyQuery() {
            return this.context.getParam("query_row") != null;
        }

        public Object getQueryColumn(String column) {
            if (column == null) {
                return null;
            } else {
                return this.context.getParam("query_row") == null ? null : ((Map)this.context.getParam("query_row")).get(column);
            }
        }

        public void addFinalArg(int index, Object arg) {
            if (this.context.getParam("final_args") == null) {
                this.context.setParam("final_args", new ArrayList(20));
            }

            List<Object> finalArgs = (List)this.context.getParam("final_args");
            if (finalArgs.size() - 1 >= index) {
                finalArgs.set(index, arg);
            } else {
                for(int size = finalArgs.size(); size < index; ++size) {
                    finalArgs.add(size, (Object)null);
                }

                finalArgs.add(arg);
            }

        }

        public SQLParamExtractorPipeline.ParamExtractor next() {
            return this.next;
        }

        public abstract void extract();

        public abstract String desc();

        public String toString() {
            return this.desc();
        }
    }

    protected static class KeyValuePair {
        String column;
        Object value;

        public KeyValuePair(String column, Object value) {
            this.column = column;
            this.value = value;
        }

        public String getColumn() {
            return this.column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public Object getValue() {
            return this.value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    public abstract static class PrimaryKeyExtractor {
        protected SQLParamExtractorPipeline context;

        public PrimaryKeyExtractor(SQLParamExtractorPipeline context) {
            this.context = context;
        }

        protected Object extractFromRecord(String primaryKey) {
            if (primaryKey == null) {
                return null;
            } else {
                return this.context.getParam("query_row") == null ? null : ((Map)this.context.getParam("query_row")).get(primaryKey);
            }
        }

        protected Object extractFromArgs(int index) {
            Object[] args = (Object[])((Object[])this.context.getParam("outter_args"));
            return args[index];
        }

        public List<SQLParamExtractorPipeline.KeyValuePair> render() {
            SQLParamExtractorPipeline.logger.debug(this.desc());
            return this.extract();
        }

        public abstract List<SQLParamExtractorPipeline.KeyValuePair> extract();

        public abstract String shard();

        public abstract String desc();

        public String toString() {
            return this.desc();
        }
    }

    public abstract static class UpdateSetItemExtractor extends SQLParamExtractorPipeline.ParamExtractor {
        public UpdateSetItemExtractor(SQLParamExtractorPipeline context) {
            super(context);
        }
    }

    public abstract static class OriginalParamExtractor extends SQLParamExtractorPipeline.ParamExtractor {
        public OriginalParamExtractor(SQLParamExtractorPipeline context) {
            super(context);
        }
    }
}

