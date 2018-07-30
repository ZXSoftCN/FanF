package com.zxsoft.fanfanfamily.mort.repository;

import com.zxsoft.fanfanfamily.base.domain.mort.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeDao extends JpaRepository<Employee,String> {
    //一组惯用查询方法
    Optional<Employee> findFirstByCode(String code);
    Optional<Employee> findFirstByName(String name);
    //------------!!!   Containing 等价于Sql查询中的like.而不是find*ByNameLike.
    Optional<Employee> findFirstByNameContaining(String namePart);
    Optional<Employee> findFirstByCodeOrName(String code,String name);
    Page<Employee> findByNameContaining(String nameLike, Pageable page);
    List<Employee> findAllByNameContaining(String nameLike);
    Page<Employee> findByCodeStartingWith(String codeStart,Pageable page);
    List<Employee> findAllByCodeStartingWith(String codeStart);

    //扩展
    Page<Employee> queryByOrganization_Code(String orgCode,Pageable pageable);
    List<Employee> queryAllByOrganization_Code(String orgCode);
}
