package com.unifae.med.dao;

import com.unifae.med.entity.DisciplinaTurma;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DisciplinaTurmaDAO extends GenericDAO<DisciplinaTurma, Integer> {

    public DisciplinaTurmaDAO() {
        super(DisciplinaTurma.class);
    }

    /**
     * Encontra todos os vínculos de disciplina para uma turma específica,
     * otimizado com JOIN FETCH para evitar múltiplas consultas.
     *
     * @param turmaId O ID da turma.
     * @return Uma lista de objetos DisciplinaTurma.
     */
    public List<DisciplinaTurma> findByTurmaId(Integer turmaId) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT dt FROM DisciplinaTurma dt "
                    + "JOIN FETCH dt.disciplina d "
                    + "LEFT JOIN FETCH dt.professor p "
                    + "WHERE dt.turma.idTurma = :turmaId ORDER BY d.nomeDisciplina";

            TypedQuery<DisciplinaTurma> query = em.createQuery(jpql, DisciplinaTurma.class);
            query.setParameter("turmaId", turmaId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
