const express = require('express');
const cors = require('cors');
const compression = require('compression');

const app = express();
app.use(cors());
app.use(express.json());
app.use(compression()); // Рівень 4: Стиснення даних

let users = [
    { id: 1, name: "Станіслав", friends: [2, 3, 4] },
    { id: 2, name: "Марія", friends: [1, 3] },
    { id: 3, name: "Андрій", friends: [1, 2, 5] },
    { id: 4, name: "Олена", friends: [1] },
    { id: 5, name: "Дмитро", friends: [3] },
    { id: 6, name: "Світлана", friends: [] }
];

let posts = [
    { id: 1, userId: 1, author: "Станіслав", text: "Сьогодні захищаю лабораторну з Node.js! Побажайте успіху.", likes: 12 },
    { id: 2, userId: 2, author: "Марія", text: "Хто знає, як правильно центрувати div? 😅", likes: 8 },
    { id: 3, userId: 3, author: "Андрій", text: "Вийшла нова версія React. Треба протестувати.", likes: 24 },
    { id: 4, userId: 4, author: "Олена", text: "Нарешті вихідні! Час відпочити від коду.", likes: 45 },
    { id: 5, userId: 5, author: "Дмитро", text: "Node.js + Express — це сила. Дуже зручно будувати API.", likes: 19 },
    { id: 6, userId: 2, author: "Марія", text: "Кава і код — ідеальний ранок.", likes: 31 }
];

// Рівень 1: Отримання постів
app.get('/api/posts', (req, res) => {
    res.json(posts);
});

// Рівень 2: Додавання поста
app.post('/api/posts', (req, res) => {
    const newPost = {
        id: Date.now(),
        userId: 1, 
        author: "Станіслав", 
        text: req.body.text,
        likes: 0
    };
    posts.unshift(newPost);
    res.status(201).json(newPost);
});

// Рівень 3: Пошук користувачів
app.get('/api/users/search', (req, res) => {
    const { name } = req.query;
    const filteredUsers = users.filter(u => u.name.toLowerCase().includes(name.toLowerCase()));
    res.json(filteredUsers);
});

const PORT = 3000;
app.listen(PORT, () => console.log(`Сервер працює на порту ${PORT}`));