import React, { useState, useEffect, useRef } from 'react';
import { Search, Mic, Filter, X, TrendingUp, Clock, Star } from 'lucide-react';
import { youtubeAPI } from '../lib/api';

const SearchBar = ({ onSearch, placeholder = "Search videos...", className = "" }) => {
  const [query, setQuery] = useState('');
  const [suggestions, setSuggestions] = useState([]);
  const [showSuggestions, setShowSuggestions] = useState(false);
  const [isSearching, setIsSearching] = useState(false);
  const [searchHistory, setSearchHistory] = useState([]);
  const [showFilters, setShowFilters] = useState(false);
  const [filters, setFilters] = useState({
    regionCode: 'US',
    maxResults: 20,
    order: 'relevance',
    type: 'video'
  });
  
  const searchRef = useRef(null);
  const suggestionsRef = useRef(null);

  // Load search history from localStorage
  useEffect(() => {
    const savedHistory = localStorage.getItem('searchHistory');
    if (savedHistory) {
      setSearchHistory(JSON.parse(savedHistory));
    }
  }, []);

  // Save search history to localStorage
  const saveSearchHistory = (newQuery) => {
    const updatedHistory = [newQuery, ...searchHistory.filter(q => q !== newQuery)].slice(0, 10);
    setSearchHistory(updatedHistory);
    localStorage.setItem('searchHistory', JSON.stringify(updatedHistory));
  };

  // Handle search submission
  const handleSearch = async (searchQuery = query) => {
    if (!searchQuery.trim()) return;

    setIsSearching(true);
    setShowSuggestions(false);
    saveSearchHistory(searchQuery);

    try {
      const results = await youtubeAPI.searchVideos(searchQuery, filters.maxResults, filters.regionCode);
      onSearch(results);
    } catch (error) {
      console.error('Search error:', error);
    } finally {
      setIsSearching(false);
    }
  };

  // Handle input change with debounced suggestions
  const handleInputChange = (e) => {
    const value = e.target.value;
    setQuery(value);
    
    if (value.trim().length > 2) {
      // Generate suggestions based on common search terms
      const commonTerms = [
        'music', 'gaming', 'tutorial', 'news', 'sports', 'comedy',
        'education', 'technology', 'cooking', 'travel', 'fitness',
        'science', 'history', 'documentary', 'reviews', 'vlogs'
      ];
      
      const filteredSuggestions = commonTerms
        .filter(term => term.toLowerCase().includes(value.toLowerCase()))
        .map(term => `${value} ${term}`)
        .slice(0, 5);
      
      setSuggestions(filteredSuggestions);
      setShowSuggestions(true);
    } else {
      setSuggestions([]);
      setShowSuggestions(false);
    }
  };

  // Handle suggestion click
  const handleSuggestionClick = (suggestion) => {
    setQuery(suggestion);
    setShowSuggestions(false);
    handleSearch(suggestion);
  };

  // Handle keyboard navigation
  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      handleSearch();
    } else if (e.key === 'Escape') {
      setShowSuggestions(false);
      searchRef.current?.blur();
    }
  };

  // Handle voice search (placeholder)
  const handleVoiceSearch = () => {
    if ('webkitSpeechRecognition' in window) {
      const recognition = new window.webkitSpeechRecognition();
      recognition.continuous = false;
      recognition.interimResults = false;
      recognition.lang = 'en-US';
      
      recognition.onresult = (event) => {
        const transcript = event.results[0][0].transcript;
        setQuery(transcript);
        handleSearch(transcript);
      };
      
      recognition.start();
    } else {
      alert('Voice search is not supported in this browser');
    }
  };

  // Click outside to close suggestions
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (searchRef.current && !searchRef.current.contains(event.target) &&
          suggestionsRef.current && !suggestionsRef.current.contains(event.target)) {
        setShowSuggestions(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  return (
    <div className={`relative ${className}`}>
      {/* Search Input */}
      <div className="relative">
        <div className="relative flex items-center">
          <div className="absolute left-3 text-gray-400">
            <Search size={20} />
          </div>
          
          <input
            ref={searchRef}
            type="text"
            value={query}
            onChange={handleInputChange}
            onKeyDown={handleKeyDown}
            onFocus={() => setShowSuggestions(true)}
            placeholder={placeholder}
            className="w-full pl-10 pr-20 py-3 bg-gray-800 border border-gray-700 rounded-full text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-red-500 focus:border-transparent transition-all duration-200"
          />
          
          <div className="absolute right-3 flex items-center space-x-2">
            {isSearching && (
              <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-red-500"></div>
            )}
            
            {query && (
              <button
                onClick={() => {
                  setQuery('');
                  setSuggestions([]);
                  setShowSuggestions(false);
                }}
                className="text-gray-400 hover:text-white transition-colors"
              >
                <X size={18} />
              </button>
            )}
            
            <button
              onClick={handleVoiceSearch}
              className="text-gray-400 hover:text-white transition-colors"
              title="Voice search"
            >
              <Mic size={18} />
            </button>
            
            <button
              onClick={() => setShowFilters(!showFilters)}
              className={`transition-colors ${showFilters ? 'text-red-500' : 'text-gray-400 hover:text-white'}`}
              title="Search filters"
            >
              <Filter size={18} />
            </button>
          </div>
        </div>
      </div>

      {/* Search Filters */}
      {showFilters && (
        <div className="absolute top-full left-0 right-0 mt-2 bg-gray-800 border border-gray-700 rounded-lg p-4 z-50">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-300 mb-2">Region</label>
              <select
                value={filters.regionCode}
                onChange={(e) => setFilters({...filters, regionCode: e.target.value})}
                className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-white text-sm focus:outline-none focus:ring-2 focus:ring-red-500"
              >
                <option value="US">United States</option>
                <option value="GB">United Kingdom</option>
                <option value="CA">Canada</option>
                <option value="AU">Australia</option>
                <option value="DE">Germany</option>
                <option value="FR">France</option>
                <option value="ES">Spain</option>
                <option value="MX">Mexico</option>
                <option value="BR">Brazil</option>
                <option value="IN">India</option>
              </select>
            </div>
            
            <div>
              <label className="block text-sm font-medium text-gray-300 mb-2">Results</label>
              <select
                value={filters.maxResults}
                onChange={(e) => setFilters({...filters, maxResults: parseInt(e.target.value)})}
                className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-white text-sm focus:outline-none focus:ring-2 focus:ring-red-500"
              >
                <option value={10}>10 results</option>
                <option value={20}>20 results</option>
                <option value={50}>50 results</option>
              </select>
            </div>
            
            <div>
              <label className="block text-sm font-medium text-gray-300 mb-2">Order</label>
              <select
                value={filters.order}
                onChange={(e) => setFilters({...filters, order: e.target.value})}
                className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-white text-sm focus:outline-none focus:ring-2 focus:ring-red-500"
              >
                <option value="relevance">Relevance</option>
                <option value="date">Date</option>
                <option value="rating">Rating</option>
                <option value="viewCount">View Count</option>
                <option value="title">Title</option>
              </select>
            </div>
            
            <div>
              <label className="block text-sm font-medium text-gray-300 mb-2">Type</label>
              <select
                value={filters.type}
                onChange={(e) => setFilters({...filters, type: e.target.value})}
                className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-white text-sm focus:outline-none focus:ring-2 focus:ring-red-500"
              >
                <option value="video">Videos</option>
                <option value="channel">Channels</option>
                <option value="playlist">Playlists</option>
              </select>
            </div>
          </div>
        </div>
      )}

      {/* Search Suggestions */}
      {showSuggestions && (suggestions.length > 0 || searchHistory.length > 0) && (
        <div
          ref={suggestionsRef}
          className="absolute top-full left-0 right-0 mt-2 bg-gray-800 border border-gray-700 rounded-lg shadow-lg z-50 max-h-96 overflow-y-auto"
        >
          {/* Search History */}
          {searchHistory.length > 0 && (
            <div className="p-3 border-b border-gray-700">
              <div className="flex items-center space-x-2 text-gray-400 text-sm mb-2">
                <Clock size={14} />
                <span>Recent searches</span>
              </div>
              {searchHistory.slice(0, 5).map((item, index) => (
                <button
                  key={index}
                  onClick={() => handleSuggestionClick(item)}
                  className="w-full text-left px-3 py-2 text-white hover:bg-gray-700 rounded text-sm transition-colors flex items-center space-x-2"
                >
                  <Clock size={14} className="text-gray-400" />
                  <span>{item}</span>
                </button>
              ))}
            </div>
          )}

          {/* Suggestions */}
          {suggestions.length > 0 && (
            <div className="p-3">
              <div className="flex items-center space-x-2 text-gray-400 text-sm mb-2">
                <TrendingUp size={14} />
                <span>Suggestions</span>
              </div>
              {suggestions.map((suggestion, index) => (
                <button
                  key={index}
                  onClick={() => handleSuggestionClick(suggestion)}
                  className="w-full text-left px-3 py-2 text-white hover:bg-gray-700 rounded text-sm transition-colors flex items-center space-x-2"
                >
                  <Search size={14} className="text-gray-400" />
                  <span>{suggestion}</span>
                </button>
              ))}
            </div>
          )}

          {/* Quick Search Categories */}
          <div className="p-3 border-t border-gray-700">
            <div className="flex items-center space-x-2 text-gray-400 text-sm mb-2">
              <Star size={14} />
              <span>Popular categories</span>
            </div>
            <div className="flex flex-wrap gap-2">
              {['Music', 'Gaming', 'News', 'Sports', 'Education', 'Technology'].map((category) => (
                <button
                  key={category}
                  onClick={() => handleSuggestionClick(category)}
                  className="px-3 py-1 bg-gray-700 hover:bg-gray-600 text-white text-xs rounded-full transition-colors"
                >
                  {category}
                </button>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default SearchBar; 