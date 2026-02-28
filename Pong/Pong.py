import pygame
from sys import exit

pygame.init()

# Settings
WIDTH, HEIGHT = 1280, 720
screen = pygame.display.set_mode((WIDTH,HEIGHT))
pygame.display.set_caption("Pong")
clock = pygame.time.Clock()
font = pygame.font.Font("freesansbold.ttf",50)

red_rect = pygame.Rect(20,20,50,100)
score1 = 0
score2 = 0

blue_rect = pygame.Rect(1210,20,50,100)


def draw_line():
    pygame.draw.line(screen,"white", (640,0), (640,720),1)

def draw_score1():
    score_surface = font.render(f"{score1}",False,"red")
    score_rect = score_surface.get_rect(center = (550, 50))
    screen.blit(score_surface,score_rect)
    return score1

def draw_score2():
    score_surface = font.render(f"{score2}",False,"blue")
    score_rect = score_surface.get_rect(center = (730, 50))
    screen.blit(score_surface,score_rect)
    return score2

# Hastighet boks
speed = 6

# sirkel
circle_x, circle_y = WIDTH // 2, HEIGHT // 2
circle_radius = 30
speed_x, speed_y = 8,8
circle_color = "white"

circle = pygame.draw.circle(screen, circle_color, (circle_x,circle_y), circle_radius)
circle_rect = pygame.Rect(circle_x - circle_radius, circle_y - circle_radius, circle_radius *2, circle_radius*2)

while True:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            exit()
    screen.fill("black")
    draw_line()
    draw_score1()
    draw_score2()
    pygame.draw.rect(screen,"red",(red_rect))
    pygame.draw.rect(screen,"blue",(blue_rect))

    circle_x += speed_x
    circle_y += speed_y

    circle_rect.center = (circle_x,circle_y)

    if circle_x - circle_radius <= 0 or circle_x + circle_radius >= WIDTH:
        speed_x = -speed_x
    if circle_y - circle_radius <= 0 or circle_y + circle_radius >= HEIGHT:
        speed_y = -speed_y

    pygame.draw.circle(screen, circle_color, (circle_x, circle_y), circle_radius)
    
    # Spiller 1
    if red_rect.colliderect(circle_rect):
        speed_x = -speed_x
    keys = pygame.key.get_pressed()
    if keys[pygame.K_s]:
        red_rect.y += speed
    if keys[pygame.K_w]:
        red_rect.y -= speed
    
    if red_rect.top <= 0:
        red_rect.top = 0
    if red_rect.bottom >= 720:
        red_rect.bottom = 720
    
    if circle_rect.left <= 0:
        score2 += 1
    if circle_rect.right >= 1280:
        score1 += 1


    # Spiller 2
    if blue_rect.colliderect(circle_rect):
        speed_x = -speed_x
    keys = pygame.key.get_pressed()
    if keys[pygame.K_DOWN]:
        blue_rect.y += speed
    if keys[pygame.K_UP]:
        blue_rect.y -= speed
    
    if blue_rect.top <= 0:
        blue_rect.top = 0
    if blue_rect.bottom >= 720:
        blue_rect.bottom = 720


    
    pygame.display.flip()
    clock.tick(60)