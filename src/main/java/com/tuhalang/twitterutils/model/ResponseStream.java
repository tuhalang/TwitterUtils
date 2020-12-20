package com.tuhalang.twitterutils.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseStream {

    private Tweet data;

    @JsonProperty(value = "matching_rules")
    private Rule[] rules;

    public Tweet getData() {
        return data;
    }

    public void setData(Tweet data) {
        this.data = data;
    }

    public Rule[] getRules() {
        return rules;
    }

    public void setRules(Rule[] rules) {
        this.rules = rules;
    }

    public String getRulesStr(){
        String rulesStr = "";
        for(Rule rule : rules){
            rulesStr += rule.getTag()+";";
        }
        return rulesStr;
    }
}
