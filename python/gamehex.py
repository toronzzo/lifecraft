import random
import pygame
import sys
import math

# Определение класса организма
class Organism:
    def __init__(self, x, y, symbol, strength, initiative, name):
        self.x = x
        self.y = y
        self.symbol = symbol
        self.strength = strength
        self.initiative = initiative
        self.alive = True
        self.age = 0
        self.name = name

    def move(self, board):
        pass

    def fight(self, other):
        if isinstance(other, Human) and self.symbol != 'W':
            self.alive = False
        else:
            if self.strength >= other.strength:
                other.alive = False
            else:
                self.alive = False

    def reproduce(self, board):
        pass

class Animal(Organism):
    def move(self, board):
        direction = random.choice(['up', 'down', 'left', 'right'])
        if direction == 'up' and self.y > 0:
            self.y -= 1
        elif direction == 'down' and self.y < len(board) - 1:
            self.y += 1
        elif direction == 'left' and self.x > 0:
            self.x -= 1
        elif direction == 'right' and self.x < len(board[0]) - 1:
            self.x += 1

    def reproduce(self, board):
        empty_cells = self.get_adjacent_empty_cells(board)
        if empty_cells:
            new_x, new_y = random.choice(empty_cells)
            return Animal(new_x, new_y, self.symbol, self.strength, self.initiative, self.name)
        return None

    def get_adjacent_empty_cells(self, board):
        directions = [('up', 0, -1), ('down', 0, 1), ('left', -1, 0), ('right', 1, 0)]
        empty_cells = []
        for direction, dx, dy in directions:
            new_x, new_y = self.x + dx, self.y + dy
            if 0 <= new_x < len(board[0]) and 0 <= new_y < len(board) and board[new_y][new_x] == ' ':
                empty_cells.append((new_x, new_y))
        return empty_cells

class Human(Animal):
    def __init__(self, x, y, symbol, strength, initiative, name):
        super().__init__(x, y, symbol, strength, initiative, name)
        self.ability_active = False
        self.ability_timer = 0
        self.cooldown_timer = 0

    def move(self, board, direction=None):
        if direction == 'up' and self.y > 0:
            self.y -= 1
        elif direction == 'down' and self.y < len(board) - 1:
            self.y += 1
        elif direction == 'left' and self.x > 0:
            self.x -= 1
        elif direction == 'right' and self.x < len(board[0]) - 1:
            self.x += 1

    def activate_ability(self):
        if self.cooldown_timer == 0:
            self.ability_active = True
            self.ability_timer = 5  # Длительность способности
            self.cooldown_timer = 10  # Время перезарядки

    def update_timers(self):
        if self.ability_active:
            self.ability_timer -= 1
            if self.ability_timer <= 0:
                self.ability_active = False
        if self.cooldown_timer > 0:
            self.cooldown_timer -= 1

    def destroy_nearby(self, game_board):
        directions = [(0, -1), (0, 1), (-1, 0), (1, 0), (-1, -1), (1, 1), (-1, 1), (1, -1)]
        for dx, dy in directions:
            nx, ny = self.x + dx, self.y + dy
            for organism in game_board.organisms:
                if organism.x == nx and organism.y == ny and organism.alive:
                    organism.alive = False

class Plant(Organism):
    def move(self, board):
        pass

    def reproduce(self, board):
        empty_cells = self.get_adjacent_empty_cells(board)
        if empty_cells:
            new_x, new_y = random.choice(empty_cells)
            return Plant(new_x, new_y, self.symbol, self.strength, self.initiative, self.name)
        return None

    def get_adjacent_empty_cells(self, board):
        directions = [('up', 0, -1), ('down', 0, 1), ('left', -1, 0), ('right', 1, 0)]
        empty_cells = []
        for direction, dx, dy in directions:
            new_x, new_y = self.x + dx, self.y + dy
            if 0 <= new_x < len(board[0]) and 0 <= new_y < len(board) and board[new_y][new_x] == ' ':
                empty_cells.append((new_x, new_y))
        return empty_cells

class GameBoard:
    def __init__(self, width, height):
        self.width = width
        self.height = height
        self.board = [[' ' for _ in range(width)] for _ in range(height)]
        self.organisms = []

    def add_organism(self, organism):
        self.organisms.append(organism)
        self.board[organism.y][organism.x] = organism.symbol

    def move_organisms(self, human_direction=None):
        for organism in self.organisms:
            if not organism.alive:
                continue
            self.board[organism.y][organism.x] = ' '
            if isinstance(organism, Human):
                if human_direction:
                    organism.move(self.board, human_direction)
                organism.update_timers()
            else:
                organism.move(self.board)
            self.resolve_interactions(organism)
            if organism.alive:
                self.board[organism.y][organism.x] = organism.symbol

    def resolve_interactions(self, organism):
        for other in self.organisms:
            if other != organism and other.alive and other.x == organism.x and other.y == organism.y:
                organism.fight(other)
                if organism.alive and not isinstance(organism, Human):  # Человек не размножается
                    new_organism = organism.reproduce(self.board)
                    if new_organism:
                        self.add_organism(new_organism)

    def remove_dead_organisms(self):
        self.organisms = [org for org in self.organisms if org.alive]

    def save_game(self, filename):
        with open(filename, 'w') as f:
            for organism in self.organisms:
                f.write(f'{organism.symbol} {organism.x} {organism.y} {organism.strength} {organism.initiative} {organism.age}\n')

    def load_game(self, filename):
        with open(filename, 'r') as f:
            self.organisms = []
            self.board = [[' ' for _ in range(self.width)] for _ in range(self.height)]
            for line in f:
                symbol, x, y, strength, initiative, age = line.strip().split()
                x, y, strength, initiative, age = map(int, [x, y, strength, initiative, age])
                if symbol == 'H':
                    organism = Human(x, y, symbol, strength, initiative, 'Human')
                elif symbol == 'A':
                    organism = Animal(x, y, symbol, strength, initiative, 'Animal')
                elif symbol == 'P':
                    organism = Plant(x, y, symbol, strength, initiative, 'Plant')
                elif symbol == 'W':
                    organism = Animal(x, y, symbol, strength, initiative, 'Wolf')
                elif symbol == 'B':
                    organism = Animal(x, y, symbol, strength, initiative, 'Bear')
                self.add_organism(organism)

    def display(self, screen, cell_size):
        screen.fill((255, 255, 255))
        for y in range(self.height):
            for x in range(self.width):
                pygame.draw.polygon(screen, (0, 0, 0), self.hexagon_points(x, y, cell_size), 1)

        for organism in self.organisms:
            if organism.symbol == 'H':
                color = (0, 0, 255)  # Синий
            elif organism.symbol == 'A':
                color = (255, 0, 0)  # Красный
            elif organism.symbol == 'P':
                color = (0, 255, 0)  # Зеленый
            elif organism.symbol == 'F':
                color = (255, 165, 0)  # Оранжевый (лиса)
            elif organism.symbol == 'T':
                color = (139, 69, 19)  # Коричневый (черепаха)
            elif organism.symbol == 'W':
                color = (0, 0, 0)  # Черный (волк)
            elif organism.symbol == 'B':
                color = (165, 42, 42)  # Коричневый (медведь)

            points = self.hexagon_points(organism.x, organism.y, cell_size)
            pygame.draw.polygon(screen, color, points)
            font = pygame.font.SysFont(None, 24)
            text = font.render(organism.name[0], True, (255, 255, 255))
            text_rect = text.get_rect(center=(sum([p[0] for p in points]) / 6, sum([p[1] for p in points]) / 6))
            screen.blit(text, text_rect)
        pygame.display.flip()

    def hexagon_points(self, x, y, size):
        angle = math.pi / 3
        x_offset = size * 3 / 2 * x
        y_offset = size * math.sqrt(3) * (y + 0.5 * (x % 2))
        points = [(x_offset + size * math.cos(angle * i), y_offset + size * math.sin(angle * i)) for i in range(6)]
        return points

def create_organism(x, y, organism_type):
    if organism_type == 'H':
        return Human(x, y, 'H', strength=10, initiative=5, name='Human')
    elif organism_type == 'A':
        return Animal(x, y, 'A', strength=random.randint(1, 10), initiative=random.randint(1, 5), name='Animal')
    elif organism_type == 'P':
        return Plant(x, y, 'P', strength=0, initiative=0, name='Plant')
    elif organism_type == 'F':
        return Animal(x, y, 'F', strength=7, initiative=4, name='Fox')
    elif organism_type == 'T':
        return Animal(x, y, 'T', strength=3, initiative=1, name='Turtle')
    elif organism_type == 'W':
        return Animal(x, y, 'W', strength=8, initiative=5, name='Wolf')
    elif organism_type == 'B':
        return Animal(x, y, 'B', strength=9, initiative=2, name='Bear')

def main():
    pygame.init()
    cell_size = 30
    width, height = 10, 10  # Размер игрового поля
    screen = pygame.display.set_mode((width * cell_size * 2, height * cell_size * 2))
    pygame.display.set_caption("Организмы")

    game_board = GameBoard(width, height)
    human = Human(5, 5, 'H', strength=10, initiative=5, name='Human')
    game_board.add_organism(human)

    animal1 = Animal(2, 2, 'A', strength=5, initiative=3, name='Animal')
    animal2 = Animal(7, 7, 'A', strength=6, initiative=4, name='Animal')
    fox = Animal(3, 3, 'F', strength=7, initiative=4, name='Fox')
    turtle = Animal(8, 8, 'T', strength=3, initiative=1, name='Turtle')
    wolf = Animal(1, 1, 'W', strength=8, initiative=5, name='Wolf')
    bear = Animal(9, 9, 'B', strength=9, initiative=2, name='Bear')

    game_board.add_organism(animal1)
    game_board.add_organism(animal2)
    game_board.add_organism(fox)
    game_board.add_organism(turtle)
    game_board.add_organism(wolf)
    game_board.add_organism(bear)

    for _ in range(10):  # Добавим еще животных
        x, y = random.randint(0, width-1), random.randint(0, height-1)
        animal = Animal(x, y, 'A', strength=random.randint(1, 10), initiative=random.randint(1, 5), name='Animal')
        game_board.add_organism(animal)

    plant1 = Plant(4, 4, 'P', strength=0, initiative=0, name='Plant')
    plant2 = Plant(6, 6, 'P', strength=0, initiative=0, name='Plant')
    game_board.add_organism(plant1)
    game_board.add_organism(plant2)

    clock = pygame.time.Clock()
    running = True
    human_moved = False

    organism_types = ['H', 'A', 'P', 'F', 'T', 'W', 'B']
    creating_organism = False
    organism_type = None
    selected_position = None

    while running:
        human_direction = None
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
            elif event.type == pygame.KEYDOWN:
                if event.key == pygame.K_UP:
                    human_direction = 'up'
                elif event.key == pygame.K_DOWN:
                    human_direction = 'down'
                elif event.key == pygame.K_LEFT:
                    human_direction = 'left'
                elif event.key == pygame.K_RIGHT:
                    human_direction = 'right'
                elif event.key == pygame.K_SPACE:
                    human.activate_ability()
                elif event.key == pygame.K_p:
                    human.destroy_nearby(game_board)
                elif event.key == pygame.K_RETURN and human_moved:
                    game_board.move_organisms(human_direction)
                    game_board.remove_dead_organisms()
                    human_moved = False
                elif event.key == pygame.K_c:  # Нажмите "C" для создания существа
                    creating_organism = True
                    print("Choose organism type to create:")
                    for i, ot in enumerate(organism_types):
                        print(f"{i}: {ot}")
                    choice = int(input("Enter your choice: "))
                    organism_type = organism_types[choice]
                elif event.key == pygame.K_s:  # Нажмите "S" для сохранения игры
                    game_board.save_game('savegame.txt')
                    print("Game saved!")
                elif event.key == pygame.K_l:  # Нажмите "L" для загрузки игры
                    game_board.load_game('savegame.txt')
                    print("Game loaded!")
            elif event.type == pygame.MOUSEBUTTONDOWN and creating_organism:
                if event.button == 1:  # Левая кнопка мыши
                    mouse_x, mouse_y = event.pos
                    grid_x, grid_y = mouse_x // cell_size, mouse_y // cell_size
                    if game_board.board[grid_y][grid_x] == ' ':
                        selected_position = (grid_x, grid_y)
                        new_organism = create_organism(grid_x, grid_y, organism_type)
                        game_board.add_organism(new_organism)
                        creating_organism = False

        if human_direction and not human_moved:
            game_board.move_organisms(human_direction)
            human_moved = True

        game_board.display(screen, cell_size)
        clock.tick(1)  # 1 ход в секунду

    pygame.quit()
    sys.exit()

if __name__ == "__main__":
    main()
