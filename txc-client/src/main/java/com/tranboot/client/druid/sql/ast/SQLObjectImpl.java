package com.tranboot.client.druid.sql.ast;

import com.tranboot.client.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class SQLObjectImpl implements SQLObject {
    private SQLObject parent;
    protected Map<String, Object> attributes;

    public SQLObjectImpl() {
    }

    public final void accept(SQLASTVisitor visitor) {
        if (visitor == null) {
            throw new IllegalArgumentException();
        } else {
            visitor.preVisit(this);
            this.accept0(visitor);
            visitor.postVisit(this);
        }
    }

    protected abstract void accept0(SQLASTVisitor var1);

    protected final void acceptChild(SQLASTVisitor visitor, List<? extends SQLObject> children) {
        if (children != null) {
            Iterator var3 = children.iterator();

            while(var3.hasNext()) {
                SQLObject child = (SQLObject)var3.next();
                this.acceptChild(visitor, child);
            }

        }
    }

    protected final void acceptChild(SQLASTVisitor visitor, SQLObject child) {
        if (child != null) {
            child.accept(visitor);
        }
    }

    public void output(StringBuffer buf) {
        buf.append(super.toString());
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        this.output(buf);
        return buf.toString();
    }

    public SQLObject getParent() {
        return this.parent;
    }

    public void setParent(SQLObject parent) {
        this.parent = parent;
    }

    public Map<String, Object> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new HashMap(1);
        }

        return this.attributes;
    }

    public Object getAttribute(String name) {
        return this.attributes == null ? null : this.attributes.get(name);
    }

    public void putAttribute(String name, Object value) {
        if (this.attributes == null) {
            this.attributes = new HashMap(1);
        }

        this.attributes.put(name, value);
    }

    public Map<String, Object> getAttributesDirect() {
        return this.attributes;
    }

    public void addBeforeComment(String comment) {
        if (comment != null) {
            if (this.attributes == null) {
                this.attributes = new HashMap(1);
            }

            List<String> comments = (List)this.attributes.get("format.before_comment");
            if (comments == null) {
                comments = new ArrayList(2);
                this.attributes.put("format.before_comment", comments);
            }

            ((List)comments).add(comment);
        }
    }

    public void addBeforeComment(List<String> comments) {
        if (this.attributes == null) {
            this.attributes = new HashMap(1);
        }

        List<String> attrComments = (List)this.attributes.get("format.before_comment");
        if (attrComments == null) {
            this.attributes.put("format.before_comment", comments);
        } else {
            attrComments.addAll(comments);
        }

    }

    public List<String> getBeforeCommentsDirect() {
        return this.attributes == null ? null : (List)this.attributes.get("format.before_comment");
    }

    public void addAfterComment(String comment) {
        if (this.attributes == null) {
            this.attributes = new HashMap(1);
        }

        List<String> comments = (List)this.attributes.get("format.after_comment");
        if (comments == null) {
            comments = new ArrayList(2);
            this.attributes.put("format.after_comment", comments);
        }

        ((List)comments).add(comment);
    }

    public void addAfterComment(List<String> comments) {
        if (this.attributes == null) {
            this.attributes = new HashMap(1);
        }

        List<String> attrComments = (List)this.attributes.get("format.after_comment");
        if (attrComments == null) {
            this.attributes.put("format.after_comment", comments);
        } else {
            attrComments.addAll(comments);
        }

    }

    public List<String> getAfterCommentsDirect() {
        return this.attributes == null ? null : (List)this.attributes.get("format.after_comment");
    }

    public boolean hasBeforeComment() {
        List<String> comments = this.getBeforeCommentsDirect();
        if (comments == null) {
            return false;
        } else {
            return !comments.isEmpty();
        }
    }

    public boolean hasAfterComment() {
        List<String> comments = this.getAfterCommentsDirect();
        if (comments == null) {
            return false;
        } else {
            return !comments.isEmpty();
        }
    }
}

