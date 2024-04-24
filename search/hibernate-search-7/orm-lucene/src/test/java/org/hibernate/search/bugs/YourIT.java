package org.hibernate.search.bugs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LONG;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;

import org.hibernate.search.util.common.SearchException;
import org.junit.jupiter.api.Test;

public class YourIT extends SearchTestBase {

	@Override
	public Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { AnnotatedEntity.class };
	}

	@Test
	public void testNormally() throws InterruptedException {
		try ( Session s = getSessionFactory().openSession() ) {
			AnnotatedEntity yourEntity1 = new AnnotatedEntity( 1L, "Jane Smith" );
			AnnotatedEntity yourEntity2 = new AnnotatedEntity( 2L, "John Doe" );

			Transaction tx = s.beginTransaction();
			s.persist( yourEntity1 );
			s.persist( yourEntity2 );
			tx.commit();

			SearchSession searchSession = Search.session( s );

			List<AnnotatedEntity> hits = searchSession.search( AnnotatedEntity.class )
					.where( f -> f.match().field( "name" ).matching( "smith" ) )
					.fetchHits( 20 );

			assertThat( hits )
					.hasSize( 1 )
					.element( 0 ).extracting( AnnotatedEntity::getId )
					.isEqualTo( 1L );
		}
	}

	@Test
	public void testWithMassIndexer() throws InterruptedException {
		try ( Session s = getSessionFactory().openSession() ) {
			AnnotatedEntity yourEntity1 = new AnnotatedEntity( 1L, "Jane Smith" );
			AnnotatedEntity yourEntity2 = new AnnotatedEntity( 2L, "John Doe" );

			Transaction tx = s.beginTransaction();
			s.persist( yourEntity1 );
			s.persist( yourEntity2 );
			tx.commit();

			SearchSession searchSession = Search.session( s );

			MassIndexer massIndexer = searchSession.massIndexer();
			massIndexer.startAndWait();

			List<AnnotatedEntity> hits = searchSession.search( AnnotatedEntity.class )
					.where( f -> f.match().field( "name" ).matching( "smith" ) )
					.fetchHits( 20 );

			assertThat( hits )
					.hasSize( 1 )
					.element( 0 ).extracting( AnnotatedEntity::getId )
					.isEqualTo( 1L );
		}
	}

}
