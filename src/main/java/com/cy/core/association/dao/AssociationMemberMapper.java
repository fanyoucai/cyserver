package com.cy.core.association.dao;

import com.cy.core.association.entity.AssociationMember;


import java.util.List;
import java.util.Map;

/**
 * Created by cha0res on 12/13/16.
 */
public interface AssociationMemberMapper {

    List<AssociationMember> selectAssociationMember(Map<String, Object> map);

    long count(Map<String, Object> map);

    void save(AssociationMember associationMember);

    void update(AssociationMember associationMember);

    void delete(List<String> list);
}
