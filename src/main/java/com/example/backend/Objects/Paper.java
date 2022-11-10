package com.example.backend.Objects;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 
 * @TableName paper
 */
@Data
public class Paper implements Serializable {
    /**
     * 
     */
    @TableId(value = "pid", type = IdType.ASSIGN_ID)
    private Long pid;

    /**
     * 
     */
    private String paperName;

    /**
     * 
     */
    private String authors;

    /**
     * 
     */
    private String institues;

    /**
     * 
     */
    private String journal;

    /**
     * 
     */
    private Integer year;

    /**
     * 
     */
    private String keywords;

    /**
     * 
     */
    private String abs;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Paper other = (Paper) that;
        return (this.getPid() == null ? other.getPid() == null : this.getPid().equals(other.getPid()))
            && (this.getPaperName() == null ? other.getPaperName() == null : this.getPaperName().equals(other.getPaperName()))
            && (this.getAuthors() == null ? other.getAuthors() == null : this.getAuthors().equals(other.getAuthors()))
            && (this.getInstitues() == null ? other.getInstitues() == null : this.getInstitues().equals(other.getInstitues()))
            && (this.getJournal() == null ? other.getJournal() == null : this.getJournal().equals(other.getJournal()))
            && (this.getYear() == null ? other.getYear() == null : this.getYear().equals(other.getYear()))
            && (this.getKeywords() == null ? other.getKeywords() == null : this.getKeywords().equals(other.getKeywords()))
            && (this.getAbs() == null ? other.getAbs() == null : this.getAbs().equals(other.getAbs()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPid() == null) ? 0 : getPid().hashCode());
        result = prime * result + ((getPaperName() == null) ? 0 : getPaperName().hashCode());
        result = prime * result + ((getAuthors() == null) ? 0 : getAuthors().hashCode());
        result = prime * result + ((getInstitues() == null) ? 0 : getInstitues().hashCode());
        result = prime * result + ((getJournal() == null) ? 0 : getJournal().hashCode());
        result = prime * result + ((getYear() == null) ? 0 : getYear().hashCode());
        result = prime * result + ((getKeywords() == null) ? 0 : getKeywords().hashCode());
        result = prime * result + ((getAbs() == null) ? 0 : getAbs().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", pid=").append(pid);
        sb.append(", paperName=").append(paperName);
        sb.append(", authors=").append(authors);
        sb.append(", institues=").append(institues);
        sb.append(", journal=").append(journal);
        sb.append(", year=").append(year);
        sb.append(", keywords=").append(keywords);
        sb.append(", abs=").append(abs);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}