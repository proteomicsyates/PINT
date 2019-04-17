package edu.scripps.yates.persistence.mysql;

import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.junit.Test;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.HibernateUtil;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.Sample;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.SampleAdapter;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import junit.framework.Assert;

public class SessionTests {

	@Test
	public void transationsTest() {
		try {
			ContextualSessionHandler.beginGoodTransaction();
			ContextualSessionHandler.finishGoodTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void saveDelteAndRetrievingTest() {
		try {
			ContextualSessionHandler.beginGoodTransaction();
			final edu.scripps.yates.utilities.proteomicsmodel.factories.SampleEx sample2 = new edu.scripps.yates.utilities.proteomicsmodel.factories.SampleEx(
					"Mi muestra");
			sample2.setDescription("Description");

			sample2.setWildType(false);
			final Sample sample = new SampleAdapter(sample2).adapt();
			ContextualSessionHandler.save(sample);
			ContextualSessionHandler.finishGoodTransaction();
			final Sample savedSample = ContextualSessionHandler.load(sample.getId(), Sample.class);
			Assert.assertNotNull(savedSample);
			Assert.assertEquals(savedSample.getName(), "Mi muestra");
			Assert.assertEquals(savedSample.getDescription(), "Description");
			final Integer id = savedSample.getId();
			System.out.println(id + " " + savedSample.getName() + " " + savedSample.getDescription());

			final Object sample4 = ContextualSessionHandler.load(id, Sample.class);
			Assert.assertNotNull(sample4);

			// delete
			ContextualSessionHandler.beginGoodTransaction();
			ContextualSessionHandler.delete(sample4);
			ContextualSessionHandler.finishGoodTransaction();
			final Object sample5 = ContextualSessionHandler.load(id, Sample.class);
			Assert.assertNull(sample5);
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void retrievingSameObjectTwice() {
		ContextualSessionHandler.beginGoodTransaction();
		final Psm psmPSM = ContextualSessionHandler.load(4764, Psm.class);
		final Set<Protein> proteinsPSM = psmPSM.getProteins();
		System.out.println(proteinsPSM.size() + " proteins");

		final Protein protein1 = ContextualSessionHandler.load(1000, Protein.class);
		System.out.println(protein1.hashCode());
		System.out.println(protein1.getPsms().size() + " PSMs");
		final Iterator<Psm> iterator = protein1.getPsms().iterator();

		while (iterator.hasNext()) {
			final Psm next = iterator.next();
			System.out.println("PSM:" + next.getId() + " having " + next.getProteins().size() + " proteins");
			iterator.remove();
		}
		System.out.println(protein1.getPsms().size() + " PSMs");
		ContextualSessionHandler.finishGoodTransaction();

		final Iterator<Psm> iterator2 = protein1.getPsms().iterator();

		while (iterator2.hasNext()) {
			final Psm next = iterator2.next();
			System.out.println("PSM:" + next.getId() + " having " + next.getProteins().size() + " proteins");

		}

		final Protein protein2 = ContextualSessionHandler.load(1000, Protein.class);
		System.out.println(protein2.hashCode());
		System.out.println(protein2.getPsms().size() + " PSMs");
		Assert.assertEquals(protein1, protein2);

		System.out.println("Loading psm 4764");
		// load PSM 4764
		final Psm psm = ContextualSessionHandler.load(4764, Psm.class);
		final Set<Protein> proteins = psm.getProteins();
		System.out.println(proteins.size() + " proteins");
		for (final Protein protein : proteins) {
			System.out.println("Protein:" + protein.getId());
		}
		final Iterator<Protein> iterator3 = proteins.iterator();
		while (iterator3.hasNext()) {
			iterator3.next();
			iterator3.remove();
		}

		final Psm psm2 = ContextualSessionHandler.load(4764, Psm.class);
		final Set<Protein> proteins2 = psm2.getProteins();
		System.out.println(proteins2.size() + " proteins");

	}

	@Test
	public void persistenceUtilsTest() {
		final Set<Psm> psms = new THashSet<Psm>();
		for (int i = 100; i < 200; i++) {
			final Psm psm = ContextualSessionHandler.load(i, Psm.class);
			psms.add(psm);
		}
		System.out.println(psms.size() + " PSMs retrieved");

		final Map<String, Collection<Psm>> map = new THashMap<String, Collection<Psm>>();
		PersistenceUtils.addToPSMMapByPsmId(map, psms);
		final Map<String, Collection<Protein>> proteinsMap = PersistenceUtils.getProteinsFromPsms(map, true);
		System.out.println(proteinsMap.size() + " proteins in map");
		final Set<Protein> proteinSet = new THashSet<Protein>();
		for (final Collection<Protein> proteinSet2 : proteinsMap.values()) {
			proteinSet.addAll(proteinSet2);
		}
		System.out.println(proteinSet.size() + " proteins in set");
		final Map<String, Collection<Psm>> psmsMap = PersistenceUtils.getPsmsFromProteins(proteinSet, true);
		System.out.println(psmsMap.size() + " PSMs");
		final Set<Psm> psmSet = new THashSet<Psm>();
		for (final Collection<Psm> psmSet2 : psmsMap.values()) {
			psmSet.addAll(psmSet2);
		}
		System.out.println(psmSet.size() + " PSMs in set");

		for (final Psm psm : psmSet) {
			System.out.println("PSM " + psm.getId() + " has " + psm.getProteins().size() + " proteins");
		}

	}

	@Test
	public void otroTest() {
		final Psm psm = ContextualSessionHandler.load(170, Psm.class);
		final Set<Protein> proteins = psm.getProteins();
		System.out.println("PSM " + psm.getId() + " has " + psm.getProteins().size() + " proteins");

		final Protein protein = proteins.iterator().next();
		System.out.println("Protein " + protein.getId() + " has " + protein.getPsms().size() + " psms");
		System.out.println("Removing peptides of protein " + protein.getId());
		final Iterator psmIterator = protein.getPsms().iterator();
		while (psmIterator.hasNext()) {
			psmIterator.next();
			psmIterator.remove();
		}
		System.out.println("Protein " + protein.getId() + " has " + protein.getPsms().size() + " psms");
		System.out.println("PSM " + psm.getId() + " has " + psm.getProteins().size() + " proteins");

		final Set<Protein> set = new THashSet<Protein>();
		set.add(protein);
		final Map<String, Collection<Psm>> psmsFromProteins = PersistenceUtils.getPsmsFromProteins(set, true);
		System.out.println(psmsFromProteins.size());

		final Set<Psm> set2 = new THashSet<Psm>();
		set2.add(psm);
		final Map<String, Collection<Protein>> proteinsFromPsms = PersistenceUtils.getProteinsFromPsms(set2, true);
		System.out.println(proteinsFromPsms.size());

	}

	@Test
	public void otroTest2() {
		final Protein protein = ContextualSessionHandler.load(34, Protein.class);
		final Psm psm = (Psm) protein.getPsms().iterator().next();
		final Integer psmID = psm.getId();
		System.out.println(psm.getId() + " " + psm.getSequence());
		psm.setSequence("ASDFASDFASDF");
		final Psm psm2 = ContextualSessionHandler.load(psmID, Psm.class);
		Assert.assertEquals(psm, psm2);
		System.out.println(psm.getId() + " " + psm.getSequence());
		System.out.println(psm2.getId() + " " + psm2.getSequence());

	}

	@Test
	public void otroTest3() {
		final Protein protein = ContextualSessionHandler.load(34, Protein.class);
		final Psm psm = (Psm) protein.getPsms().iterator().next();
		final Integer psmID = psm.getId();
		System.out.println(psm.getId() + " " + psm.getSequence());
		psm.setSequence("ASDFASDFASDF");
		final Psm psm2 = ContextualSessionHandler.load(psmID, Psm.class);
		Assert.assertEquals(psm, psm2);
		System.out.println(psm.getId() + " " + psm.getSequence());
		System.out.println(psm2.getId() + " " + psm2.getSequence());

	}

	@Test
	public void hibernateFilterTest() {
		try {
			final SessionFactory sf = HibernateUtil
					.getInstance("salvador", "natjeija", "jdbc:mysql://localhost:3306/interactome_db")
					.getSessionFactory();
			org.hibernate.Session currentSession = sf.openSession();
			currentSession.setFlushMode(FlushMode.NEVER);
			ManagedSessionContext.bind(currentSession);

			currentSession.beginTransaction();

			// do staf

			currentSession = HibernateUtil
					.getInstance("salvador", "natjeija", "jdbc:mysql://localhost:3306/interactome_db")
					.getSessionFactory().getCurrentSession();
			currentSession.beginTransaction();
			currentSession.getTransaction().commit();
			// end staf
			currentSession = ManagedSessionContext.unbind(sf);

			System.out.println("Closing signal");

			currentSession = sf.openSession();
			currentSession.setFlushMode(FlushMode.NEVER);
			ManagedSessionContext.bind(currentSession);
			currentSession.beginTransaction();

			currentSession = ManagedSessionContext.unbind(sf);
			currentSession.flush();

			currentSession.getTransaction().commit();

			currentSession.close();
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}

	}
}
