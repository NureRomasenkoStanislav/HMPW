import React, { useState } from 'react';

function TodoApp() {
  const [todos] = useState([
    { id: 1, text: "Прочитати лекцію про JSON", completed: true },
    { id: 2, text: "Виконати практичну №2", completed: false },
    { id: 3, text: "Налаштувати React проект", completed: true },
    { id: 4, text: "Захистити роботу", completed: false },
  ]);

  const [filterStatus, setFilterStatus] = useState('all');
  const [searchQuery, setSearchQuery] = useState('');

  const filteredTodos = todos.filter(todo => {

    const matchesStatus = 
      filterStatus === 'all' || 
      (filterStatus === 'completed' && todo.completed) || 
      (filterStatus === 'pending' && !todo.completed);
    
    const matchesSearch = todo.text.toLowerCase().includes(searchQuery.toLowerCase());

    return matchesStatus && matchesSearch;
  });

  return (
    <div style={{ padding: '40px', fontFamily: 'Arial', backgroundColor: '#121417', color: 'white', minHeight: '100vh', textAlign: 'center' }}>
      <h1>Мій список завдань</h1>

      {}
      <div style={{ marginBottom: '20px' }}>
        <input 
          type="text" 
          placeholder="Пошук завдань..." 
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          style={{
            padding: '10px',
            width: '300px',
            borderRadius: '5px',
            border: '1px solid #444',
            backgroundColor: '#222',
            color: 'white',
            fontSize: '16px'
          }}
        />
      </div>

      <div style={{ marginBottom: '20px' }}>
        <button onClick={() => setFilterStatus('all')} style={btnStyle(filterStatus === 'all')}>Усі</button>
        <button onClick={() => setFilterStatus('completed')} style={btnStyle(filterStatus === 'completed')}>Виконані</button>
        <button onClick={() => setFilterStatus('pending')} style={btnStyle(filterStatus === 'pending')}>У процесі</button>
      </div>

      <div style={{ display: 'inline-block', textAlign: 'left', minWidth: '400px' }}>
        {filteredTodos.map(todo => (
          <div key={todo.id} style={{
            padding: '15px',
            borderBottom: '1px solid #333',
            display: 'flex',
            alignItems: 'center',
            opacity: todo.completed ? 0.6 : 1
          }}>
            <span style={{ marginRight: '10px' }}>{todo.completed ? '✅' : '⏳'}</span>
            <span style={{ textDecoration: todo.completed ? 'line-through' : 'none' }}>
              {todo.text}
            </span>
          </div>
        ))}
      </div>
    </div>
  );
}

const btnStyle = (isActive) => ({
  marginRight: '10px',
  padding: '8px 16px',
  backgroundColor: isActive ? '#007bff' : '#fff',
  color: isActive ? 'white' : 'black',
  border: 'none',
  borderRadius: '4px',
  cursor: 'pointer',
  fontWeight: 'bold'
});

export default TodoApp;