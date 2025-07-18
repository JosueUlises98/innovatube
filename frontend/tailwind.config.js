/** @type {import('tailwindcss').Config} */
export default {
  darkMode: 'class',
  content: [
    './index.html',
    './src/**/*.{js,jsx,ts,tsx}',
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Roboto', 'Arial', 'sans-serif'],
      },
      colors: {
        youtube: {
          dark: '#181818',
          darker: '#0f0f0f',
          card: '#212121',
          border: '#303030',
          text: '#fff',
          textMuted: '#aaa',
          accent: '#ff0000',
          gray: {
            lighter: '#e5e5e5',
            dark: '#222',
            darker: '#181818',
          },
        },
      },
      boxShadow: {
        'youtube-lg': '0 10px 15px -3px rgba(255,0,0,0.1), 0 4px 6px -4px rgba(255,0,0,0.1)',
        'youtube-xl': '0 20px 25px -5px rgba(255,0,0,0.15), 0 8px 10px -6px rgba(255,0,0,0.15)',
      },
    },
  },
  plugins: [require('tailwind-scrollbar')],
}; 