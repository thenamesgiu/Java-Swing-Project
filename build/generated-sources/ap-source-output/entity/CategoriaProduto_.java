package entity;

import entity.Produto;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-11-13T21:00:39", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(CategoriaProduto.class)
public class CategoriaProduto_ { 

    public static volatile ListAttribute<CategoriaProduto, Produto> produtos;
    public static volatile SingularAttribute<CategoriaProduto, Integer> id;
    public static volatile SingularAttribute<CategoriaProduto, String> descricao;

}