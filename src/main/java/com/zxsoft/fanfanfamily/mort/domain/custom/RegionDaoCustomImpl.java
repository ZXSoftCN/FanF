package com.zxsoft.fanfanfamily.mort.domain.custom;

import com.zxsoft.fanfanfamily.base.domain.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 扩展JpaRepository，可进行Criteria和原始sql语句的查询
 */
public class RegionDaoCustomImpl extends JdbcDaoSupport implements RegionDaoCustom {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public RegionDaoCustomImpl(DataSource ds) {
        setDataSource(ds);
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    //标准条件查询
    @Override
    public List<Region> customQueryCriteria() {

        CriteriaQuery<Region> criteriaQuery = em.getCriteriaBuilder().createQuery(Region.class);
        criteriaQuery.select(criteriaQuery.from(Region.class));
        return em.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Region> customQuery() {

        return getJdbcTemplate().query("select * from sys_region",new RegionRowMapper());
    }

    private static class RegionRowMapper implements RowMapper<Region> {

        /*
         * (non-Javadoc)
         * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
         */
        public Region mapRow(ResultSet rs, int rowNum) throws SQLException {

            Region item = new Region();
            item.setId(rs.getString("id"));
            item.setCode(rs.getString("code"));
            item.setName(rs.getString("name"));

            return item;
        }
    }
}
