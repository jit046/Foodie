import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FaSearch, FaFilter, FaMapMarkerAlt, FaStar, FaClock } from 'react-icons/fa';
import RestaurantCard from '../components/RestaurantCard';
import LoadingSpinner from '../components/LoadingSpinner';
import axios from 'axios';
import './Restaurants.css';

const Restaurants = () => {
  const [restaurants, setRestaurants] = useState([]);
  const [filteredRestaurants, setFilteredRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('');
  const [selectedMealType, setSelectedMealType] = useState('');
  const [minRating, setMinRating] = useState(0);
  const [showFilters, setShowFilters] = useState(false);

  const categories = ['Indian', 'Chinese', 'Italian', 'Mexican', 'Thai', 'Fast Food', 'Desserts'];
  const mealTypes = [
    { value: 'BREAKFAST', label: 'Breakfast' },
    { value: 'LUNCH', label: 'Lunch' },
    { value: 'DINNER', label: 'Dinner' }
  ];

  useEffect(() => {
    fetchRestaurants();
  }, []);

  useEffect(() => {
    filterRestaurants();
  }, [restaurants, searchTerm, selectedCategory, selectedMealType, minRating]);

  const fetchRestaurants = async () => {
    try {
      setLoading(true);
      const response = await axios.get('/api/restaurants');
      setRestaurants(response.data);
    } catch (error) {
      setError('Failed to load restaurants');
      console.error('Error fetching restaurants:', error);
    } finally {
      setLoading(false);
    }
  };

  const filterRestaurants = () => {
    let filtered = restaurants;

    // Search by name
    if (searchTerm) {
      filtered = filtered.filter(restaurant =>
        restaurant.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        restaurant.description?.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    // Filter by category
    if (selectedCategory) {
      filtered = filtered.filter(restaurant =>
        restaurant.categories?.includes(selectedCategory)
      );
    }

    // Filter by meal type availability
    if (selectedMealType) {
      filtered = filtered.filter(restaurant => {
        switch (selectedMealType) {
          case 'BREAKFAST':
            return restaurant.breakfastWindow;
          case 'LUNCH':
            return restaurant.lunchWindow;
          case 'DINNER':
            return restaurant.dinnerWindow;
          default:
            return true;
        }
      });
    }

    // Filter by minimum rating
    if (minRating > 0) {
      filtered = filtered.filter(restaurant => restaurant.rating >= minRating);
    }

    setFilteredRestaurants(filtered);
  };

  const clearFilters = () => {
    setSearchTerm('');
    setSelectedCategory('');
    setSelectedMealType('');
    setMinRating(0);
  };

  const getActiveFiltersCount = () => {
    let count = 0;
    if (searchTerm) count++;
    if (selectedCategory) count++;
    if (selectedMealType) count++;
    if (minRating > 0) count++;
    return count;
  };

  if (loading) {
    return <LoadingSpinner text="Loading restaurants..." />;
  }

  return (
    <div className="restaurants-page">
      <div className="container">
        {/* Header */}
        <div className="page-header">
          <h1 className="page-title">Restaurants</h1>
          <p className="page-description">
            Discover amazing restaurants and order your favorite meals
          </p>
        </div>

        {/* Search and Filters */}
        <div className="search-filters">
          <div className="search-bar">
            <FaSearch className="search-icon" />
            <input
              type="text"
              placeholder="Search restaurants..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="search-input"
            />
          </div>

          <button
            className={`filter-toggle ${showFilters ? 'active' : ''}`}
            onClick={() => setShowFilters(!showFilters)}
          >
            <FaFilter className="filter-icon" />
            Filters
            {getActiveFiltersCount() > 0 && (
              <span className="filter-count">{getActiveFiltersCount()}</span>
            )}
          </button>
        </div>

        {/* Filter Panel */}
        {showFilters && (
          <div className="filter-panel">
            <div className="filter-group">
              <label className="filter-label">Category</label>
              <select
                value={selectedCategory}
                onChange={(e) => setSelectedCategory(e.target.value)}
                className="filter-select"
              >
                <option value="">All Categories</option>
                {categories.map(category => (
                  <option key={category} value={category}>{category}</option>
                ))}
              </select>
            </div>

            <div className="filter-group">
              <label className="filter-label">Meal Type</label>
              <select
                value={selectedMealType}
                onChange={(e) => setSelectedMealType(e.target.value)}
                className="filter-select"
              >
                <option value="">All Meal Types</option>
                {mealTypes.map(meal => (
                  <option key={meal.value} value={meal.value}>{meal.label}</option>
                ))}
              </select>
            </div>

            <div className="filter-group">
              <label className="filter-label">Minimum Rating</label>
              <select
                value={minRating}
                onChange={(e) => setMinRating(parseFloat(e.target.value))}
                className="filter-select"
              >
                <option value={0}>Any Rating</option>
                <option value={3}>3+ Stars</option>
                <option value={4}>4+ Stars</option>
                <option value={4.5}>4.5+ Stars</option>
              </select>
            </div>

            <button onClick={clearFilters} className="clear-filters-btn">
              Clear Filters
            </button>
          </div>
        )}

        {/* Results */}
        <div className="results-section">
          <div className="results-header">
            <h2 className="results-title">
              {filteredRestaurants.length} Restaurant{filteredRestaurants.length !== 1 ? 's' : ''} Found
            </h2>
            {getActiveFiltersCount() > 0 && (
              <button onClick={clearFilters} className="clear-all-btn">
                Clear All Filters
              </button>
            )}
          </div>

          {error ? (
            <div className="error-message">
              <p>{error}</p>
              <button onClick={fetchRestaurants} className="btn btn-primary">
                Try Again
              </button>
            </div>
          ) : filteredRestaurants.length === 0 ? (
            <div className="no-results">
              <FaSearch className="no-results-icon" />
              <h3>No restaurants found</h3>
              <p>Try adjusting your search criteria or filters</p>
              <button onClick={clearFilters} className="btn btn-primary">
                Clear Filters
              </button>
            </div>
          ) : (
            <div className="restaurants-grid">
              {filteredRestaurants.map(restaurant => (
                <RestaurantCard key={restaurant.id} restaurant={restaurant} />
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Restaurants;
