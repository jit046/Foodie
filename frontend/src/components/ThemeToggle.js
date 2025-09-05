import React from 'react';
import { FaSun, FaMoon } from 'react-icons/fa';
import { useTheme } from '../contexts/ThemeContext';
import './ThemeToggle.css';

const ThemeToggle = () => {
  const { theme, toggleTheme, isDark } = useTheme();

  return (
    <div className="theme-toggle">
      <button 
        className={`theme-btn ${isDark ? 'active' : ''}`}
        onClick={toggleTheme}
        title={`Switch to ${isDark ? 'light' : 'dark'} theme`}
      >
        {isDark ? <FaSun className="theme-icon" /> : <FaMoon className="theme-icon" />}
        <span className="theme-text">
          {isDark ? 'Light' : 'Dark'}
        </span>
      </button>
    </div>
  );
};

export default ThemeToggle;
