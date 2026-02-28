#Sjakk

import pygame

pygame.init()
WIDTH = 800
HEIGHT = 720
screen = pygame.display.set_mode([WIDTH, HEIGHT])
pygame.display.set_caption("Sjakk from kvante-PC")
font = pygame.font.Font("freesansbold.ttf", 16)
medium_font = pygame.font.Font("freesansbold.ttf", 30)
big_font = pygame.font.Font("freesansbold.ttf", 40)
timer = pygame.time.Clock()
fps = 60

# Spill variabler og bilder
white_pieces = ["rook", "knight", "bishop", "king", "queen", "bishop", "knight", "rook",
                "pawn", "pawn", "pawn", "pawn", "pawn", "pawn", "pawn", "pawn"]
white_locations = [(0,0), (1,0), (2,0), (3,0), (4,0), (5,0), (6,0), (7,0),
                   (0,1), (1,1), (2,1), (3,1), (4,1), (5,1), (6,1), (7,1)]
black_locations = [(0,7), (1,7), (2,7), (3,7), (4,7), (5,7), (6,7), (7,7),
                   (0,6), (1,6), (2,6), (3,6), (4,6), (5,6), (6,6), (7,6)]
black_pieces = ["rook", "knight", "bishop", "king", "queen", "bishop", "knight", "rook",
                "pawn", "pawn", "pawn", "pawn", "pawn", "pawn", "pawn", "pawn"]
captured_pieces_white = []
captured_pieces_black = []

# 0 - white turn no selection: 1 - whites turn piece selected: 2 - black turn no selection: 3 - black turn piece selected
turn_step = 0
selection = 100
valid_moves = []

# Laste inn bilder av sjakkbrikkene (queen, king, rook, bisop, knight, pawn) * 2
black_queen = pygame.image.load("C:/Users/David/Favorites/spill_kode/sjakk/sjakkbrikker/black queen.png")
black_queen = pygame.transform.scale(black_queen, (64,64))
black_queen_small = pygame.transform.scale(black_queen, (36,36))
black_king = pygame.image.load("C:/Users/David/Favorites/spill_kode/sjakk/sjakkbrikker/black king.png")
black_king = pygame.transform.scale(black_king, (64,64))
black_king_small = pygame.transform.scale(black_king, (36,36))
black_rook = pygame.image.load("C:/Users/David/Favorites/spill_kode/sjakk/sjakkbrikker/black rook.png")
black_rook = pygame.transform.scale(black_rook, (64,64))
black_rook_small = pygame.transform.scale(black_rook, (36,36))
black_bishop = pygame.image.load("C:/Users/David/Favorites/spill_kode/sjakk/sjakkbrikker/black bishop.png")
black_bishop = pygame.transform.scale(black_bishop, (64,64))
black_bishop_small = pygame.transform.scale(black_bishop, (36,36))
black_knight = pygame.image.load("C:/Users/David/Favorites/spill_kode/sjakk/sjakkbrikker/black knight.png")
black_knight = pygame.transform.scale(black_knight, (64,64))
black_knight_small = pygame.transform.scale(black_knight, (36,36))
black_pawn = pygame.image.load("C:/Users/David/Favorites/spill_kode/sjakk/sjakkbrikker/black pawn.png")
black_pawn = pygame.transform.scale(black_pawn, (52,52))
black_pawn_small = pygame.transform.scale(black_pawn, (36,36))
white_queen = pygame.image.load("C:/Users/David/Favorites/spill_kode/sjakk/sjakkbrikker/white queen.png")
white_queen = pygame.transform.scale(white_queen, (64,64))
white_queen_small = pygame.transform.scale(white_queen, (36,36))
white_king = pygame.image.load("C:/Users/David/Favorites/spill_kode/sjakk/sjakkbrikker/white king.png")
white_king = pygame.transform.scale(white_king, (64,64))
white_king_small = pygame.transform.scale(white_king, (36,36))
white_rook = pygame.image.load("C:/Users/David/Favorites/spill_kode/sjakk/sjakkbrikker/white rook.png")
white_rook = pygame.transform.scale(white_rook, (64,64))
white_rook_small = pygame.transform.scale(white_rook, (36,36))
white_bishop = pygame.image.load("C:/Users/David/Favorites/spill_kode/sjakk/sjakkbrikker/white bishop.png")
white_bishop = pygame.transform.scale(white_bishop, (64,64))
white_bishop_small = pygame.transform.scale(white_bishop, (36,36))
white_knight = pygame.image.load("C:/Users/David/Favorites/spill_kode/sjakk/sjakkbrikker/white knight.png")
white_knight = pygame.transform.scale(white_knight, (64,64))
white_knight_small = pygame.transform.scale(white_knight, (36,36))
white_pawn = pygame.image.load("C:/Users/David/Favorites/spill_kode/sjakk/sjakkbrikker/white pawn.png")
white_pawn = pygame.transform.scale(white_pawn, (52,52))
white_pawn_small = pygame.transform.scale(white_pawn, (36,36))
white_images = [white_pawn, white_queen, white_king, white_knight, white_rook, white_bishop]
small_white_images = [white_pawn_small, white_queen_small, white_king_small, white_knight_small,
                      white_rook_small, white_bishop_small]
black_images = [black_pawn, black_queen, black_king, black_knight, black_rook, black_bishop]
small_black_images = [black_pawn_small, black_queen_small, black_king_small, black_knight_small,
                      black_rook_small, black_bishop_small]
piece_list = ["pawn", "queen", "king", "knight", "rook", "bishop"]
# Sjekke variabler / flashing counter
counter = 0
winner = ""
game_over = False

# Tegne sjakkbrettet
def draw_board():
    for i in range(32):
        kolonne = i % 4
        rad = i // 4
        if rad % 2 == 0:
            pygame.draw.rect(screen, "light gray", [480 - (kolonne * 160), rad * 80, 80, 80])
        else:
            pygame.draw.rect(screen, "light gray", [560 - (kolonne * 160), rad * 80, 80, 80])
    pygame.draw.rect(screen, "gray", [0, 640, WIDTH, 80])
    pygame.draw.rect(screen, "gold", [0, 640, WIDTH, 80], 5)
    pygame.draw.rect(screen, "gold", [640, 0, 200, HEIGHT], 5)

    status_text = ["Hvit: Velg en brikke", "Hvit: Velg en plass å flytte til",
                   "Svart: Velg en brikke", "Svart: Velg en plass å flytte til"]

    screen.blit(big_font.render(status_text[turn_step], True, "black"), (16, 656))

    pygame.draw.line(screen, "black", (0, 640), (640, 640), 2)
    pygame.draw.line(screen, "black", (0,640), (0,0), 2)
    pygame.draw.line(screen, "black", (0,0), (640, 0), 2)
    pygame.draw.line(screen, "black", (640,640), (640, 0), 2)

    screen.blit(medium_font.render("GI OPP", True, "black"), (670, 664))



# Tegne brikker på sjakkbrettet
def draw_pieces():
    for i in range(len(white_pieces)):
        index = piece_list.index(white_pieces[i])
        if white_pieces[i] == "pawn":
            screen.blit(white_pawn, (white_locations[i][0] * 80 + 15, white_locations[i][1] * 80 + 20))
        else:
            screen.blit(white_images[index], (white_locations[i][0] * 80 + 8, white_locations[i][1] * 80 + 8))
        if turn_step < 2:
            if selection == i:
                pygame.draw.rect(screen, "red", [white_locations[i][0] * 80 + 1, white_locations[i][1] * 80 + 1, 80, 80,], 2)


    for i in range(len(black_pieces)):
        index = piece_list.index(black_pieces[i])
        if black_pieces[i] == "pawn":
            screen.blit(black_pawn, (black_locations[i][0] * 80 + 15, black_locations[i][1] * 80 + 20))
        else:
            screen.blit(black_images[index], (black_locations[i][0] * 80 + 8, black_locations[i][1] * 80 + 8))
        if turn_step >= 2:
            if selection == i:
                pygame.draw.rect(screen, "blue", [black_locations[i][0] * 80 + 1, black_locations[i][1] * 80 + 1, 80, 80], 2)


# Funksjon for å sjekke alle brikker sine gyldige muligheter på bretttet
def check_options(pieces, locations, turn):
    moves_list = []
    all_moves_list = []
    for i in range(len(pieces)):
        location = locations[i]
        piece = pieces[i]
        if piece == "pawn":
            moves_list = check_pawn(location, turn)
        elif piece == "rook":
            moves_list = check_rook(location, turn)
        elif piece == "knight":
            moves_list = check_knight(location, turn)
        elif piece == "bishop":
            moves_list = check_bishop(location, turn)
        elif piece == "queen":
            moves_list = check_queen(location, turn)
        elif piece == "king":
            moves_list = check_king(location, turn)
        all_moves_list.append(moves_list)

    return all_moves_list


# Sjekke gyldige trekk for bønder
def check_pawn(position, color):
    moves_list = []
    if color == "white":
        if (position[0], position[1] + 1) not in white_locations and (position[0], position[1] + 1) not in black_locations and position[1] < 7:
            moves_list.append((position[0], position[1] + 1)) #Hvis ruten foran ikke er tatt av noen brikke så er det mulig å gå en frem
        if (position[0], position[1] + 2) not in white_locations and (position[0], position[1] + 2) not in black_locations and position[1] == 1:
            moves_list.append((position[0], position[1] + 2))  #Hvis man er i startposisjon er det mulig å gå to frem
        if (position[0] + 1, position[1] + 1) in black_locations: #Hvis ruten til høyre diagonal inneholder en fiende-brikke kan man gå dit
            moves_list.append((position[0] + 1, position[1] + 1))
        if (position[0] - 1, position[1] + 1) in black_locations: #Hvis ruten til venstre diagonal inneholder en fiende-brikke kan man gå dit
            moves_list.append((position[0] - 1, position[1] + 1))
    else: #Hvis fargen ikke er hvit må den være svart (som betyr else)
        if (position[0], position[1] - 1) not in white_locations and (position[0], position[1] - 1) not in black_locations and position[1] > 0:
            moves_list.append((position[0], position[1] - 1)) #Hvis ruten foran ikke er tatt av noen brikke så er det mulig å gå en frem
        if (position[0], position[1] - 2) not in white_locations and (position[0], position[1] - 2) not in black_locations and position[1] == 6:
            moves_list.append((position[0], position[1] - 2))  #Hvis man er i startposisjon er det mulig å gå to frem
        if (position[0] + 1, position[1] - 1) in white_locations: #Hvis ruten til høyre diagonal inneholder en fiende-brikke kan man gå dit
            moves_list.append((position[0] + 1, position[1] - 1))
        if (position[0] - 1, position[1] - 1) in white_locations: #Hvis ruten til venstre diagonal inneholder en fiende-brikke kan man gå dit
            moves_list.append((position[0] - 1, position[1] - 1))
    return moves_list

#Sjekke gyldige trekk for tårn
def check_rook(position, color):
    moves_list = []
    if color == "white":
        enemy_list = black_locations
        friends_list = white_locations
    else:
        friends_list = black_locations
        enemy_list = white_locations
    for i in range(4): #ned, opp, høyre, venstre
        path = True
        chain = 1
        if i == 0: #Hvis ned
            x = 0
            y = 1
        elif i == 1: #hvis opp
            x = 0
            y = -1
        elif i == 2: #Hvis høyre
            x = 1
            y = 0
        elif i == 3: #Hvis venstre
            x = -1
            y = 0
        while path: #Mens veien er True (eksisterer)
            if (position[0] + (chain * x), position[1] + (chain * y)) not in friends_list and 0 <= position[0] + (chain * x) <= 7 and 0 <= position[1] + (chain * y) <= 7:
                moves_list.append((position[0] + (chain * x), position[1] + (chain * y))) # Hvis man går i enhver retning: dersom det er tomt eller en fiende kan man gå en videre
                if (position[0] + (chain * x), position[1] + (chain * y)) in enemy_list: #Hvis det er en fiende, stopper veien
                    path = False
                chain += 1
            else: #Hvis ikke det er tomt eller det har en fiendebrikke på seg kan man ikke bevege seg i den retningen
                path = False # Breaking the while-loop hvis veien ikke lenger eksisterer videre
    return moves_list

#Sjekke gyldige trekk for hest
def check_knight(position, color):
    #Sjekke om hver plass en hest kan flytte er tomt eller tatt av en fiende/venn
    moves_list = []
    if color == "white":
        enemy_list = black_locations
        friends_list = white_locations
    else:
        friends_list = black_locations
        enemy_list = white_locations
    
    # 8 ruter å sjekke for hester, de kan gå to ruter i en retning og en rute i en annen
    targets = [(1,2), (1,-2), (2,1), (2,-1), (-1,2), (-1,-2), (-2,1), (-2,-1)]
    for i in range(8):
        target = (position[0] + targets[i][0], position[1] + targets[i][1])
        if target not in friends_list and 0 <= target[0] <= 7 and 0 <= target[1] <= 7: #Dersom der man vil flytte til ikke har vennebrikker og er innenfor brettet
            moves_list.append(target) #Da kan hesten flytte til denne posisjonen
    return moves_list

#Sjekke gyldige trekk for løper
def check_bishop(position, color):
    moves_list = []
    if color == "white":
        enemy_list = black_locations
        friends_list = white_locations
    else:
        friends_list = black_locations
        enemy_list = white_locations
    for i in range(4): # opp-høyre, opp-venstre, ned-høyre, ned-venstre
        path = True
        chain = 1
        if i == 0: #Hvis opp-høyre
            x = 1
            y = -1
        elif i == 1: #hvis opp-venstre
            x = -1
            y = -1
        elif i == 2: #Hvis ned-høyre
            x = 1
            y = 1
        elif i == 3: #Hvis ned-venstre
            x = -1
            y = 1
        while path: #Mens veien er True (eksisterer)
            if (position[0] + (chain * x), position[1] + (chain * y)) not in friends_list and 0 <= position[0] + (chain * x) <= 7 and 0 <= position[1] + (chain * y) <= 7:
                moves_list.append((position[0] + (chain * x), position[1] + (chain * y))) # Hvis man går i enhver retning: dersom det er tomt eller en fiende kan man gå en videre
                if (position[0] + (chain * x), position[1] + (chain * y)) in enemy_list: #Hvis det er en fiende, stopper veien
                    path = False
                chain += 1
            else: #Hvis ikke det er tomt eller det har en fiendebrikke på seg kan man ikke bevege seg i den retningen
                path = False # Breaking the while-loop hvis veien ikke lenger eksisterer videre

    return moves_list

#Sjekke gyldige trekk for dronning
def check_queen(position, color):
    moves_list = check_bishop(position, color)
    second_list = check_rook(position, color)
    for i in range(len(second_list)):
        moves_list.append(second_list[i])
    return moves_list

#Sjekke gyldige trekk for konge
def check_king(position, color):
    moves_list = []
    if color == "white":
        enemy_list = black_locations
        friends_list = white_locations
    else:
        friends_list = black_locations
        enemy_list = white_locations
     # 8 ruter å sjekke for kongene: en rute i hver retning
    targets = [(1,0), (1,1), (1,-1), (-1,0), (-1,1), (-1,-1), (0,1), (0,-1)]
    for i in range(8):
        target = (position[0] + targets[i][0], position[1] + targets[i][1])
        if target not in friends_list and 0 <= target[0] <= 7 and 0 <= target[1] <= 7: #Dersom der man vil flytte til ikke har vennebrikker og er innenfor brettet
            moves_list.append(target) #Da kan hesten flytte til denne posisjonen

    return moves_list


# Sjekke gyldige trekk for valgt brikke
def check_valid_moves():
    if turn_step < 2:
        options_list = white_options
    else:
        options_list = black_options
    valid_options = options_list[selection]
    return valid_options


# Tegne gyldige trekk
def draw_valid(moves):
    if turn_step < 2:
        color = "red"
    else:
        color = "blue"
    for i in range(len(moves)):
        pygame.draw.circle(screen, color, (moves[i][0] * 80 + 40, moves[i][1] * 80 + 40), 4)

#Tegne brikker som er tatt
def draw_captured():
    for i in range(len(captured_pieces_white)):
        captured_piece = captured_pieces_white[i]
        index = piece_list.index(captured_piece)
        screen.blit(small_black_images[index],(660, 5 + 40*i))
    for i in range(len(captured_pieces_black)):
        captured_piece = captured_pieces_black[i]
        index = piece_list.index(captured_piece)
        screen.blit(small_white_images[index],(740, 5 + 40*i))


# Tegne en blinkende rute rundt en konge i sjakk
def draw_check():
    if game_over == True:
        return
    if turn_step < 2:
        king_index = white_pieces.index("king")
        king_location = white_locations[king_index]
        for i in range(len(black_options)):
            if king_location in black_options[i]:
                if counter < 15:
                    pygame.draw.rect(screen, "dark red", [white_locations[king_index][0] * 80 + 1, white_locations[king_index][1] * 80 + 1, 80, 80], 5)
    
    else:
        king_index = black_pieces.index("king")
        king_location = black_locations[king_index]
        for i in range(len(white_options)):
            if king_location in white_options[i]:
                if counter < 15:
                    pygame.draw.rect(screen, "dark blue", [black_locations[king_index][0] * 80 + 1, black_locations[king_index][1] * 80 + 1, 80, 80], 5)
    
# Hvis spillet avsluttes
def draw_game_over():
    pygame.draw.rect(screen, "black", [160, 160, 320, 80])
    screen.blit(font.render(f"{winner} vant runden!", True, "white"), (168, 169))
    screen.blit(font.render(f"Trykk ENTER for å starte på nytt", True, "white"), (168, 192))

# Adrian
def adrian():
    pygame.draw.rect(screen, "black", [140, 160, 340, 200])
    screen.blit(big_font.render("Heter du Adrian?", True, "White"), (145, 180))



#Game Loop
black_options = check_options(black_pieces, black_locations, "black")
white_options = check_options(white_pieces, white_locations, "white")

run = True
while run:
    timer.tick(fps)
    #La kongen flashe når den er i sjakk
    if counter < 30:
        counter += 1
    else:
        counter = 0
    screen.fill("dark gray")

    draw_board() #Tegner sjakkbrett
    draw_pieces() #Tegner brikker
    draw_captured()
    #adrian()
    draw_check() #Flasher når konge i sjakk 

    if selection != 100:
        valid_moves = check_valid_moves()
        draw_valid(valid_moves) #Tegner hvilke trekk man kan gjøre
    
    #Event handler
    for event in pygame.event.get():
        #Hvis bruker vil gå ut av spillet
        if event.type == pygame.QUIT:
            run = False 
        
        #Hvis bruker trykket på sjakkbrikke
        if event.type == pygame.MOUSEBUTTONDOWN and event.button == 1:
            x_coord = event.pos[0] // 80
            y_coord = event.pos[1] // 80
            print(x_coord, y_coord)
            ###Legge inn x og y koordinater her til gi opp knappen###
            click_coords = (x_coord, y_coord) #Sjekker hvilken rute i sjakkbrettet som er blitt klikket på
            if turn_step <= 1:
                if click_coords == (8,8) or click_coords == (9,8):
                    winner = "Svart"
                if click_coords in white_locations:
                    selection = white_locations.index(click_coords)
                    if turn_step == 0:
                        turn_step = 1 #Bytter tekst fra velg til flytt for hvit
                if click_coords in valid_moves and selection != 100:
                    white_locations[selection] = click_coords #Flytter sjakkbrikken
                    if click_coords in black_locations: #Hvis hvit brikke tar svartbrikke
                        black_piece = black_locations.index(click_coords) # black_piece er hvilken indeks i svartebrikker listen som blir tatt
                        captured_pieces_white.append(black_pieces[black_piece]) # Legger til svart brikke i brikker som er tatt
                        if black_pieces[black_piece] == "king":
                            winner = "Hvit" #Hvis hvit tar svart konge vinner de
                        black_pieces.pop(black_piece) #Tar vekk svart brikke fra svartbrikke-listen
                        black_locations.pop(black_piece) #Tar vekk plassen til svartebrikklisten
                    black_options = check_options(black_pieces, black_locations, "black")
                    white_options = check_options(white_pieces, white_locations, "white")
                    turn_step = 2 #Gjør det til svart sin tur å velge brikke
                    selection = 100 #Resetter til at man ikke har valgt en brikke
                    valid_moves = [] #Resetter de gyldige trekkene man kan gjøre
                ###Legge til en if-setning her om at hvit kan gi oppp ###

            if turn_step > 1:
                if click_coords == (8,8) or click_coords == (9,8):
                    winner = "Hvit"
                if click_coords in black_locations:
                    selection = black_locations.index(click_coords)
                    if turn_step == 2:
                        turn_step = 3 #Bytter tekst fra velg til flytt for svart
                if click_coords in valid_moves and selection != 100:
                    black_locations[selection] = click_coords #Flytter sjakkbrikken
                    if click_coords in white_locations: #Hvis svart brikke tar hvitbrikke
                        white_piece = white_locations.index(click_coords) # white_piece er hvilken indeks i svartebrikker listen som blir tatt
                        captured_pieces_black.append(white_pieces[white_piece]) # Legger til hvit brikke i brikker som er tatt
                        if white_pieces[white_piece] == "king":
                            winner = "Svart" #Hvis svart tar hvit konge vinner svart
                        white_pieces.pop(white_piece) #Tar vekk hvit brikke fra hvitbrikke-listen
                        white_locations.pop(white_piece) #Tar vekk plassen til svartbrikklisten
                    black_options = check_options(black_pieces, black_locations, "black")
                    white_options = check_options(white_pieces, white_locations, "white")
                    turn_step = 0 #Gjør det til svart sin tur å velge brikke
                    selection = 100 #Resetter til at man ikke har valgt en brikke
                    valid_moves = [] #Resetter de gyldige trekkene man kan gjøre
        if event.type == pygame.KEYDOWN and game_over:
            if event.key == pygame.K_RETURN:
                game_over = False
                winner = ""
                white_pieces = ["rook", "knight", "bishop", "king", "queen", "bishop", "knight", "rook",
                                "pawn", "pawn", "pawn", "pawn", "pawn", "pawn", "pawn", "pawn"]
                white_locations = [(0,0), (1,0), (2,0), (3,0), (4,0), (5,0), (6,0), (7,0),
                                    (0,1), (1,1), (2,1), (3,1), (4,1), (5,1), (6,1), (7,1)]
                black_locations = [(0,7), (1,7), (2,7), (3,7), (4,7), (5,7), (6,7), (7,7),
                                    (0,6), (1,6), (2,6), (3,6), (4,6), (5,6), (6,6), (7,6)]
                black_pieces = ["rook", "knight", "bishop", "king", "queen", "bishop", "knight", "rook",
                                "pawn", "pawn", "pawn", "pawn", "pawn", "pawn", "pawn", "pawn"]
                captured_pieces_white = []
                captured_pieces_black = []
                turn_step = 0
                selection = 100
                valid_moves = []
                black_options = check_options(black_pieces, black_locations, "black")
                white_options = check_options(white_pieces, white_locations, "white")




    if winner != "":
        game_over = True
        draw_game_over()


    
    pygame.display.flip()
pygame.quit()

