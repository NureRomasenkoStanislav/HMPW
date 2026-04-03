# Завдання 1: Знайти найменше з трьох чисел
def find_min(a, b, c):
    result = min(a, b, c)
    print(f"Найменше число серед {a}, {b}, {c} — це {result}")

find_min(10, 5, 8)

# Завдання 2: Обертання рядка
def reverse_string(text):
    return text[::-1]

user_input = input("Введіть слово для обертання: ")
print("Результат:", reverse_string(user_input))

# Завдання 3: Перевірка на паліндром
def is_palindrome(word):
    clean_word = word.lower().replace(" ", "")
    if clean_word == clean_word[::-1]:
        return True
    return False

word = input("Введіть слово для перевірки на паліндром: ")
if is_palindrome(word):
    print(f"Так, '{word}' — це паліндром!")
else:
    print(f"Ні, '{word}' — не паліндром.")

# Завдання 4: Швидке сортування (QuickSort)
def quick_sort(arr):
    if len(arr) <= 1:
        return arr
    pivot = arr[len(arr) // 2] # Опорний елемент
    left = [x for x in arr if x < pivot]
    middle = [x for x in arr if x == pivot]
    right = [x for x in arr if x > pivot]
    return quick_sort(left) + middle + quick_sort(right)

numbers = [3, 6, 8, 10, 1, 2, 1]
print("Невідсортований масив:", numbers)
print("Відсортований масив:", quick_sort(numbers))