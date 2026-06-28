/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        dark: {
          bg: '#0a0a0f',
          bgSecondary: '#14141e',
          card: '#1a1a2e',
          surface: '#22223a',
          border: '#2a2a3e',
        },
        light: {
          bg: '#f8fafc',
          bgSecondary: '#f1f5f9',
          card: '#ffffff',
          surface: '#f8fafc',
          border: '#e2e8f0',
        },
      },
    },
  },
  plugins: [],
}