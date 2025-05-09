def is_round_trip_same(byte_array: bytes, encoding: str) -> bool:
    try:
        # Decode and re-encode to check round-trip integrity
        decoded = byte_array.decode(encoding)
        re_encoded = decoded.encode(encoding)
        return re_encoded == byte_array
    except Exception:
        return False

def decode_with_utf8_or_big5(byte_array: bytes) -> str:
    for encoding in ['utf-8', 'big5']:
        if is_round_trip_same(byte_array, encoding):
            return byte_array.decode(encoding)
    raise UnicodeDecodeError("Neither UTF-8 nor Big5 preserved round-trip integrity.")

def process_file(input_path: str, output_path: str):
    with open(input_path, 'r', encoding='utf-8') as infile, \
         open(output_path, 'w', encoding='utf-8') as outfile:
        for line in infile:
            line = line.rstrip('\n')  # Remove newline for consistent processing
            try:
                ibm850_bytes = line.encode('ibm850')
                decoded_line = decode_with_utf8_or_big5(ibm850_bytes)
                outfile.write(decoded_line + '\n')
            except Exception as e:
                print(f"Error processing line: {line}\n{e}")

# Example usage:
process_file('input.txt', 'output.txt')