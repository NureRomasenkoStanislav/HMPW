const rawData = [
    { "id": 1, "name": "Laptop", "category": "electronics" },
    { "id": 2, "name": "Shirt", "category": "clothing" },
    { "id": 3, "name": "Phone", "category": "electronics" }
];

function filterJSON(data, category) {
    return data.filter(item => item.category === category);
}

const filtered = filterJSON(rawData, "electronics");
console.log("Відфільтровані дані:", JSON.stringify(filtered, null, 2)); 

