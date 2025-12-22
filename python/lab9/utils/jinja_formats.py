from datetime import datetime


def datetimeformat(value):
    """Форматирует timestamp в читаемую дату"""
    if isinstance(value, (int, float)):
        return datetime.fromtimestamp(value).strftime('%d.%m.%Y %H:%M:%S')
    return value


def durationformat(value):
    """Форматирует длительность в секундах в читаемый формат"""
    if isinstance(value, (int, float)):
        days = value // (24 * 3600)
        hours = (value % (24 * 3600)) // 3600
        minutes = (value % 3600) // 60
        seconds = value % 60

        parts = []
        if days > 0:
            parts.append(f"{int(days)} д.")
        if hours > 0:
            parts.append(f"{int(hours)} ч.")
        if minutes > 0:
            parts.append(f"{int(minutes)} мин.")
        if seconds > 0 or not parts:
            parts.append(f"{int(seconds)} сек.")

        return ' '.join(parts)
    return value