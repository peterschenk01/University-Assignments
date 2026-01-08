import java.util.*;
import java.util.stream.Collectors;

public class GamesLibrary{
	
	Set<Platform> platformlist = new TreeSet<Platform>();
	Set<Game> gamelist = new TreeSet<Game>();
	
	public GamesLibrary() {
	}

	/**
   	* Adds a new platform to the game library.
   	*
   	* @param platform Platform (must not be null)
  	* @throws GamesLibraryException if platform is already in games library.
   	*/
	public void addPlatform(Platform platform){
		if(platform == null)
			throw new IllegalArgumentException();
		if(platformlist.contains(platform))
			throw new GamesLibraryException("");
		
		platformlist.add(platform);
	}

  /**
   * Removes the given platform and all games on this platform from the library.
   *
   * @param platform Platform to remove (along with all games), must be in
   * library and must not be null
   * @throws GamesLibraryException if platform is not in games library
   */
	public void removePlatform(Platform platform){
		if(platform == null)
			throw new IllegalArgumentException();
		if(!platformlist.contains(platform))
			throw new GamesLibraryException("");
		
		platformlist.remove(platform);
		
		for(Iterator<Game> i = gamelist.iterator(); i.hasNext(); ) {
			if(i.next().getPlatform().equals(platform))
				i.remove();
		}
	}

  /**
   * Adds a new game to the games library.
   *
   * @param game game to add (must not be null)
   *
   * @throws GamesLibraryException if game is already in library or platform of
   * game is not in game library
   */
	public void addGame(Game game){
		if(game == null)
			throw new IllegalArgumentException();
		if(gamelist.contains(game))
			throw new GamesLibraryException("");
		if(!platformlist.contains(game.getPlatform()))
			throw new GamesLibraryException("");
		
		gamelist.add(game);
	}

  /**
   * Removes a game from the game library.
   *
   * @param game game to remove, must not be null
   * @throws GamesLibraryException if game is not in library
   */
	public void removeGame(Game game){
		if(game == null)
			throw new IllegalArgumentException();
		if(!gamelist.contains(game))
			throw new GamesLibraryException("");
		
		gamelist.remove(game);
	}

  /**
   * Returns the sorted set of games.
   * The set must be ordered according to Game.compareTo
   * @return set of games as a read-only set
   */
	public Set<Game> getGamesReadOnly(){
		return Collections.unmodifiableSet(gamelist);
	}

  /**
   * Returns the sorted set of platforms.
   * The set must be ordered according to Platform.compareTo
   * @return set of platforms as a read-only set
   */
	public Set<Platform> getPlatformsReadOnly(){
		return Collections.unmodifiableSet(platformlist);
	}

  /**
   * Returns the game with the highest metacritic score.
   *
   * @return game with highest metacritic score (or null if games library is
   * empty)
   */
	public Game getBestGame(){
		return gamelist.stream().max((x1,x2) -> Integer.compare(x1.getMetacriticScore(), x2.getMetacriticScore())).orElse(null);
	}

  /**
   * Returns a map the maps a genre to the number of games of the genre in the
   * games library.
   *
   * @return map from genre to number of games of the genre
   */
	public Map<Genre, Long> getGenreCount(){
		return gamelist.stream().collect(Collectors.groupingBy(Game::getGenre, Collectors.counting()));
	}

  /**
   * Returns a map from the platforms to the set of games on that platform.
   */
	public Map<Platform, Set<Game>> getGamesForPlatform(){
		return gamelist.stream().collect(Collectors.groupingBy(Game::getPlatform, Collectors.toSet()));
	}

  /**
   * Returns a map from all platforms to their average metacritic scores.
   */
	public Map<Platform, Double> getAverageScoreForPlatform(){
		Map<Platform, Set<Game>> gamesForPlatform = getGamesForPlatform();
		
		return gamesForPlatform.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().mapToInt(Game::getMetacriticScore).average().orElse(0.0)));
	}

  /** 
   * Returns the platform with the highest average metacritic score.
   */
	public Platform getBestPlatform(){
		return getAverageScoreForPlatform().entrySet().stream().max((x1, x2) -> Double.compare(x1.getValue(), x2.getValue())).map(Map.Entry::getKey).orElse(null);
	}


  /**
   * Returns a list with all games ordered from highest to lowest metacritic
   * score.
   */
	public List<Game> sortGamesByMetacriticScore(){
		return gamelist.stream().sorted((x1,x2) -> -Integer.compare(x1.getMetacriticScore(), x2.getMetacriticScore())).collect(Collectors.toList());
	}

  /**
   * Returns a list with all games ordered, first, by release year (ascending)
   * and, second, by name.
   */
	public List<Game> sortGamesByReleaseYear(){
		return gamelist.stream().sorted((g1, g2) -> {
			int result = g1.getReleaseYear() - g2.getReleaseYear();
			
			if(result == 0)
				result = g1.getName().compareTo(g2.getName());
			
			return result;
				
			
		}).collect(Collectors.toList());
	}

	public double getAverageMetacriticScore() {
		return gamelist.stream().mapToInt(x -> x.getMetacriticScore()).average().orElse(0);
	}
	
	public List<Game> getGamesforGenre(Genre genre){
		if (genre == null)
		      throw new IllegalArgumentException();
		
		return gamelist.stream().filter(x -> x.getGenre() == genre).collect(Collectors.toList());
	}
	
	public boolean gameReleasedBetween(int begin, int end) {
		if(end < begin)
			throw new IllegalArgumentException();
		
		return gamelist.stream().anyMatch(x -> x.getReleaseYear() >= begin && x.getReleaseYear() <= end);
	}
	
	public List<Genre> getGenresWithGames(){
		return gamelist.stream().map(Game::getGenre).distinct().collect(Collectors.toList());
	}

	public List<Game> topGames(long n){
		return gamelist.stream().sorted((x1,x2) -> -Integer.compare(x1.getMetacriticScore(), x2.getMetacriticScore())).limit(n).collect(Collectors.toList());
	}
	
	public String getTopTenListString() {
		return topGames(10).stream().map(x -> new StringBuilder(x.getName())).reduce((builder, line) -> builder.append(", " + line)).orElseGet(StringBuilder::new).toString();
	}
}
