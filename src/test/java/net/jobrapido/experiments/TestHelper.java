package net.jobrapido.experiments;

import java.util.ArrayList;
import java.util.List;

import net.jobrapido.experiments.services.RandomizationService;

import com.google.inject.Inject;

public class TestHelper {
	
	public long USER_NUMBER_TO_EVALUATE = 1000000;
	
	@Inject RandomizationService randomizationService;
	
	
	public List<String> generateRandomUserIds() {

		List<String> randomeUserIds = new ArrayList<String>();
		
		for( long counter = 1; counter < USER_NUMBER_TO_EVALUATE; counter++ ){
			long randomLong = (long) ( counter * randomizationService.getRandomDouble() );
//			String randomString = randomizationService.getRandomString();
			String randomEmail = randomizationService.getRandomEmailAddress();
			
			String userId = randomizationService.getRandomBoolean() ? String.valueOf(randomLong) : randomEmail;
			
//			System.out.println(userId);
			
			randomeUserIds.add(userId);
			
		}
		
		return randomeUserIds;
	}
	
}
